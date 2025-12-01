package dev.parhamziaei.teahub.service.implement;

import dev.parhamziaei.teahub.configuration.properties.PaymentServiceProperties;
import dev.parhamziaei.teahub.entity.jpa.payment.PaymentGateway;
import dev.parhamziaei.teahub.entity.jpa.payment.Invoice;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.exception.custom.service.payment.GatewayNotFoundException;
import dev.parhamziaei.teahub.exception.custom.service.user.WalletChargeAmountTooSmallException;
import dev.parhamziaei.teahub.integration.payment_gateway.dto.TransactionGatewayResponse;
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
        PaymentGateway gatewayEntity = gatewayRepository.findById(gatewayId)
                .orElseThrow(GatewayNotFoundException::new);

        PaymentGatewayHandler paymentHandler = paymentGatewayFactory.getGateway(gatewayEntity.getGatewayType());

        Invoice invoice = invoiceRepository.findOne(Specification.allOf(InvoiceSpecification.hasInvoiceToken(invoiceToken)))
                .orElseThrow(NoSuchEntityException::new);

        TransactionGatewayResponse gatewayResponse = paymentHandler.createTransaction(invoice);

        return null;
        //note -> add a transaction and return redirect payment address
        //reminder -> continue from here !!!
    }

}
