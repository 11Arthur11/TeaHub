package dev.parhamziaei.teahub.integration.payment_gateway.handler;

import dev.parhamziaei.teahub.dto.request.payment.admin.GatewayPersistRequest;
import dev.parhamziaei.teahub.entity.jpa.payment.Invoice;
import dev.parhamziaei.teahub.enums.payment.PaymentGatewayType;
import dev.parhamziaei.teahub.integration.payment_gateway.dto.CallbackRequest;

public interface PaymentGatewayHandler {

    <T extends GatewayPersistRequest> void persistGateway(T request);
    boolean testGateway();
    PaymentGatewayType getGatewayType();
    String createPaymentGateway(Invoice invoice);
    <T extends CallbackRequest> void verifyTransaction(T callbackRequest);

}
