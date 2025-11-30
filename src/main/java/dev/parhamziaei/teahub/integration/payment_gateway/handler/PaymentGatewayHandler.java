package dev.parhamziaei.teahub.integration.payment_gateway.handler;

import dev.parhamziaei.teahub.entity.jpa.user.Invoice;
import dev.parhamziaei.teahub.enums.PaymentGatewayType;

public interface PaymentGatewayHandler {

    PaymentGatewayType getGatewayType();
    String createTransaction(Invoice invoice);

}
