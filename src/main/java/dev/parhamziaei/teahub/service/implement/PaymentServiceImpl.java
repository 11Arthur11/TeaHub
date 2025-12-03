package dev.parhamziaei.teahub.service.implement;

import dev.parhamziaei.teahub.configuration.properties.PaymentServiceProperties;
import dev.parhamziaei.teahub.entity.jpa.payment.Gateway;
import dev.parhamziaei.teahub.entity.jpa.payment.Invoice;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.InvoiceStatus;
import dev.parhamziaei.teahub.enums.PaymentGatewayType;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.exception.custom.service.payment.InvoiceException;
import dev.parhamziaei.teahub.exception.custom.service.payment.GatewayNotFoundException;
import dev.parhamziaei.teahub.exception.custom.service.user.WalletChargeAmountTooSmallException;
import dev.parhamziaei.teahub.integration.payment_gateway.aqaye_pardakht.dto.request.APCallbackRequest;
import dev.parhamziaei.teahub.integration.payment_gateway.handler.PaymentGatewayFactory;
import dev.parhamziaei.teahub.integration.payment_gateway.handler.PaymentGatewayHandler;
import dev.parhamziaei.teahub.repository.jpa.GatewayRepository;
import dev.parhamziaei.teahub.repository.jpa.InvoiceRepository;
import dev.parhamziaei.teahub.repository.jpa.UserRepository;
import dev.parhamziaei.teahub.repository.jpa.specification.InvoiceSpecification;
import dev.parhamziaei.teahub.service.interfaces.PaymentService;
import dev.parhamziaei.teahub.valueobject.Money;
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
    private final InvoiceRepository invoiceRepository;
    private final PaymentServiceProperties paymentProperties;
    private final PaymentGatewayFactory paymentGatewayFactory;
    private final GatewayRepository gatewayRepository;

    @Override
    public String createChargeWalletInvoice(Long userId, BigDecimal amount) {
        if (amount.compareTo(paymentProperties.minimumWalletChargeAmountIrt()) < 0)
            throw new WalletChargeAmountTooSmallException(paymentProperties.minimumWalletChargeAmountIrt());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Invoice invoice = new Invoice(user, new Money(amount));
        invoiceRepository.save(invoice);
        return invoice.getInvoiceToken();
    }

    @Override
    public String createPaymentGateway(String invoiceToken, Long gatewayId) {
        Gateway gatewayEntity = gatewayRepository.findById(gatewayId)
                .orElseThrow(GatewayNotFoundException::new);

        Invoice invoice = invoiceRepository.findOne(
                Specification.allOf(InvoiceSpecification.hasInvoiceToken(invoiceToken)
                )
        ).orElseThrow(NoSuchEntityException::new);

        if (!invoice.getStatus().equals(InvoiceStatus.PENDING)) {
            throw new InvoiceException("invoice is not pending for payment : " + invoiceToken);
        }

        PaymentGatewayHandler paymentHandler = paymentGatewayFactory.getGateway(gatewayEntity.getGatewayType());
        return paymentHandler.createTransaction(invoice);
    }

    @Override
    public void verifyAPCallback(APCallbackRequest callbackRequest) {
        PaymentGatewayHandler paymentHandler = paymentGatewayFactory.getGateway(PaymentGatewayType.AQAYE_PARDAKHT);
        if (paymentHandler.verifyTransaction(callbackRequest)) {
            Invoice invoice = invoiceRepository.findOne(
                    Specification.allOf(InvoiceSpecification.hasInvoiceToken(callbackRequest.getInvoiceToken())
                    )
            ).orElseThrow(NoSuchEntityException::new);

            if (invoice.getStatus().equals(InvoiceStatus.PENDING)) {
                invoice.setStatus(InvoiceStatus.PAID);
                invoice.setPaidAt(LocalDateTime.now().withNano(0));
                invoiceRepository.save(invoice);
            } else {
                throw new InvoiceException("invoice is cancelled and cannot be payment verified : " + invoice.getInvoiceToken());
            }
        }
    }

}
