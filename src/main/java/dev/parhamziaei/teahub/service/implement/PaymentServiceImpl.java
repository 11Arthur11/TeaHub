package dev.parhamziaei.teahub.service.implement;

import dev.parhamziaei.teahub.dto.response.dashboard.admin.AdminMetric;
import dev.parhamziaei.teahub.entity.jpa.payment.Gateway;
import dev.parhamziaei.teahub.entity.jpa.payment.invoice.Invoice;
import dev.parhamziaei.teahub.entity.jpa.payment.invoice.PaymentTransaction;
import dev.parhamziaei.teahub.enums.payment.InvoiceStatus;
import dev.parhamziaei.teahub.enums.payment.PaymentGatewayType;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.exception.custom.service.payment.InvoiceException;
import dev.parhamziaei.teahub.exception.custom.service.payment.GatewayNotFoundException;
import dev.parhamziaei.teahub.integration.payment_gateway.aqaye_pardakht.dto.request.APCallbackRequest;
import dev.parhamziaei.teahub.integration.payment_gateway.dto.CallbackRequest;
import dev.parhamziaei.teahub.integration.payment_gateway.handler.PaymentGatewayFactory;
import dev.parhamziaei.teahub.integration.payment_gateway.handler.PaymentGatewayHandler;
import dev.parhamziaei.teahub.repository.jpa.*;
import dev.parhamziaei.teahub.repository.jpa.aggregate.FinanceFlowAggregate;
import dev.parhamziaei.teahub.repository.jpa.specification.InvoiceSpecification;
import dev.parhamziaei.teahub.service.WalletService;
import dev.parhamziaei.teahub.service.interfaces.PaymentService;
import dev.parhamziaei.teahub.service.payment.PostPaymentRegistryFactory;
import dev.parhamziaei.teahub.utils.PersianPeriod;
import dev.parhamziaei.teahub.valueobject.TimeRange;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final InvoiceRepository invoiceRepo;
    private final PaymentGatewayFactory paymentGatewayFactory;
    private final GatewayRepository gatewayRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final WalletService walletService;
    private final AqayePardakhtGatewayRepository apRepo;
    private final PostPaymentRegistryFactory postPaymentRegistryFactory;

    @Override
    public String createPaymentGatewayUri(Long userId, String invoiceToken, Long gatewayId) {
        Gateway gatewayEntity = gatewayRepository.findById(gatewayId)
                .orElseThrow(GatewayNotFoundException::new);

        Invoice invoice = invoiceRepo.findOne(
                Specification.allOf(InvoiceSpecification.hasInvoiceToken(invoiceToken))
                        .and(InvoiceSpecification.mustHaveOwnerId(userId))
        ).orElseThrow(NoSuchEntityException::new);

        if (!invoice.getStatus().equals(InvoiceStatus.PENDING)) {
            throw new InvoiceException("invoice is not pending for payment : " + invoiceToken);
        }

        PaymentGatewayHandler paymentHandler = paymentGatewayFactory.getGateway(gatewayEntity.getType());
        return paymentHandler.createPaymentGateway(invoice);
    }

    public AdminMetric.FinanceMetric financeMetric() {
        return AdminMetric.FinanceMetric.builder()
                .totalBalance(walletService.totalBalance())
                .spending(walletService.totalSpendingComparison())
                .income(totalIncomeComparison())
                .build();
    }

    private AdminMetric.FinanceFlowComparison totalIncomeComparison() {
        TimeRange today = PersianPeriod.today();
        TimeRange yesterday = PersianPeriod.yesterday();

        TimeRange thisMonth = PersianPeriod.thisMonth();
        TimeRange lastMonth = PersianPeriod.lastMonth();

        TimeRange thisWeek = PersianPeriod.thisWeek();
        TimeRange lastWeek = PersianPeriod.lastWeek();

        FinanceFlowAggregate current = paymentTransactionRepository.aggregate(
                today.start(),
                today.end(),

                thisWeek.start(),
                thisWeek.end(),

                thisMonth.start(),
                thisMonth.end()
        );

        FinanceFlowAggregate previous = paymentTransactionRepository.aggregate(
                yesterday.start(),
                yesterday.end(),

                lastWeek.start(),
                lastWeek.end(),

                lastMonth.start(),
                lastMonth.end()
        );


        return new AdminMetric.FinanceFlowComparison(
                new AdminMetric.PeriodComparison<>(
                        BigDecimal.valueOf(current.daily().longValue()),
                        BigDecimal.valueOf(previous.daily().longValue())
                ),
                new AdminMetric.PeriodComparison<>(
                        BigDecimal.valueOf(current.weekly().longValue()),
                        BigDecimal.valueOf(previous.weekly().longValue())
                ),
                new AdminMetric.PeriodComparison<>(
                        BigDecimal.valueOf(current.monthly().longValue()),
                        BigDecimal.valueOf(previous.monthly().longValue())
                )
        );
    }

    @Override
    @Transactional
    public void verifyAPCallback(APCallbackRequest callbackRequest) {
        paymentGatewayFactory.getGateway(PaymentGatewayType.AQAYE_PARDAKHT)
                .verifyTransaction(callbackRequest);
        Invoice invoice = invoiceRepo.findOne(
                Specification.allOf(InvoiceSpecification.hasInvoiceToken(callbackRequest.getInvoiceId())
                )
        ).orElseThrow(NoSuchEntityException::new);

        if (!invoice.getStatus().equals(InvoiceStatus.PENDING)) {
            throw new InvoiceException("invoice is not pending and cannot be payment-verified: " + invoice.getInvoiceToken());
        }

        postPaymentRegistryFactory.getHandler(invoice.getPostPaymentAction().getPostPaymentType())
                .processAction(invoice);

        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setPaidAt(LocalDateTime.now().withNano(0));
        invoiceRepo.save(invoice);
        savePaymentTransaction(callbackRequest, invoice, apRepo.find()
                .orElseThrow(GatewayNotFoundException::new).getName());
    }

    @Override
    @Transactional
    public <T extends CallbackRequest> void savePaymentTransaction(
            T callbackRequest,
            Invoice invoice,
            String gatewayName
    ) {
        APCallbackRequest apCallback = (APCallbackRequest) callbackRequest;
        PaymentTransaction paymentTransaction = PaymentTransaction.builder()
                .transactionId(callbackRequest.getTransid())
                .gatewayName(gatewayName)
                .trackingId(apCallback.getTracking_number())
                .build();
        paymentTransaction.setForInvoice(invoice);
        paymentTransactionRepository.save(paymentTransaction);
    }

}
