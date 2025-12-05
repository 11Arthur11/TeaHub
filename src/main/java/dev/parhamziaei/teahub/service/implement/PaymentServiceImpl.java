package dev.parhamziaei.teahub.service.implement;

import dev.parhamziaei.teahub.configuration.properties.PaymentServiceProperties;
import dev.parhamziaei.teahub.entity.jpa.payment.Gateway;
import dev.parhamziaei.teahub.entity.jpa.payment.Invoice;
import dev.parhamziaei.teahub.entity.jpa.payment.Payment;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.InvoiceStatus;
import dev.parhamziaei.teahub.enums.PaymentGatewayType;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.exception.custom.service.payment.InvoiceException;
import dev.parhamziaei.teahub.exception.custom.service.payment.GatewayNotFoundException;
import dev.parhamziaei.teahub.exception.custom.service.user.WalletChargeAmountTooSmallException;
import dev.parhamziaei.teahub.integration.payment_gateway.aqaye_pardakht.dto.request.APCallbackRequest;
import dev.parhamziaei.teahub.integration.payment_gateway.dto.CallbackRequest;
import dev.parhamziaei.teahub.integration.payment_gateway.handler.PaymentGatewayFactory;
import dev.parhamziaei.teahub.integration.payment_gateway.handler.PaymentGatewayHandler;
import dev.parhamziaei.teahub.repository.jpa.GatewayRepository;
import dev.parhamziaei.teahub.repository.jpa.InvoiceRepository;
import dev.parhamziaei.teahub.repository.jpa.PaymentRepository;
import dev.parhamziaei.teahub.repository.jpa.UserRepository;
import dev.parhamziaei.teahub.repository.jpa.specification.InvoiceSpecification;
import dev.parhamziaei.teahub.service.interfaces.PaymentService;
import dev.parhamziaei.teahub.valueobject.Money;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final UserRepository userRepository;
    private final InvoiceRepository invoiceRepo;
    private final PaymentServiceProperties paymentProperties;
    private final PaymentGatewayFactory paymentGatewayFactory;
    private final GatewayRepository gatewayRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public String createChargeWalletInvoice(Long userId, BigDecimal amount) {
        if (amount.compareTo(paymentProperties.minimumWalletChargeAmountIrt()) < 0)
            throw new WalletChargeAmountTooSmallException(paymentProperties.minimumWalletChargeAmountIrt());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Invoice invoice = new Invoice(user, new Money(amount));
        invoiceRepo.save(invoice);
        return invoice.getInvoiceToken();
    }

    @Override
    public String createPaymentGateway(String invoiceToken, Long gatewayId) {
        Gateway gatewayEntity = gatewayRepository.findById(gatewayId)
                .orElseThrow(GatewayNotFoundException::new);

        Invoice invoice = invoiceRepo.findOne(
                Specification.allOf(InvoiceSpecification.hasInvoiceToken(invoiceToken)
                )
        ).orElseThrow(NoSuchEntityException::new);

        if (!invoice.getStatus().equals(InvoiceStatus.PENDING)) {
            throw new InvoiceException("invoice is not pending for payment : " + invoiceToken);
        }

        PaymentGatewayHandler paymentHandler = paymentGatewayFactory.getGateway(gatewayEntity.getGatewayType());
        return paymentHandler.createPaymentGate(invoice);
    }

    @Override
    @Transactional
    public void verifyAPCallback(APCallbackRequest callbackRequest) {
        PaymentGatewayHandler paymentHandler = paymentGatewayFactory.getGateway(PaymentGatewayType.AQAYE_PARDAKHT);
        if (paymentHandler.verifyTransaction(callbackRequest)) {
            Invoice invoice = invoiceRepo.findOne(
                    Specification.allOf(InvoiceSpecification.hasInvoiceToken(callbackRequest.getInvoiceId())
                    )
            ).orElseThrow(NoSuchEntityException::new);

            if (invoice.getStatus().equals(InvoiceStatus.PENDING)) {
                invoice.setStatus(InvoiceStatus.PAID);
                invoice.setPaidAt(LocalDateTime.now().withNano(0));
                invoiceRepo.save(invoice);
                savePaymentTransaction(callbackRequest, invoice, paymentHandler.getGatewayType());
            } else {
                throw new InvoiceException("invoice is cancelled and cannot be payment verified : " + invoice.getInvoiceToken());
            }
        }
    }

    @Override
    @Transactional
    public <T extends CallbackRequest> void savePaymentTransaction(
            T callbackRequest,
            Invoice invoice,
            PaymentGatewayType gatewayType
    ) {
        APCallbackRequest apCallback = (APCallbackRequest) callbackRequest;
        Payment payment = Payment.builder()
                .transactionId(callbackRequest.getTransid())
                .gateway(gatewayType)
                .trackingId(apCallback.getTracking_number())
                .build();
        payment.setForInvoice(invoice);
        paymentRepository.save(payment);
    }

}
