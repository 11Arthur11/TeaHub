package dev.parhamziaei.teahub.integration.payment_gateway.handler;

import dev.parhamziaei.teahub.entity.jpa.payment.Invoice;
import dev.parhamziaei.teahub.enums.payment.PaymentGatewayType;
import dev.parhamziaei.teahub.integration.payment_gateway.dto.CallbackRequest;

public interface PaymentGatewayHandler {

    void initialize();
    boolean testGateway();
    PaymentGatewayType getGatewayType();
    String createPaymentGateway(Invoice invoice);
    <T extends CallbackRequest> boolean verifyTransaction(T callbackRequest);

}
