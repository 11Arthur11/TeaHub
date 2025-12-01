package dev.parhamziaei.teahub.integration.payment_gateway.handler;

import dev.parhamziaei.teahub.entity.jpa.payment.Invoice;
import dev.parhamziaei.teahub.enums.PaymentGatewayType;
import dev.parhamziaei.teahub.integration.payment_gateway.dto.TransactionGatewayResponse;

public interface PaymentGatewayHandler {

    PaymentGatewayType getGatewayType();
    TransactionGatewayResponse createTransaction(Invoice invoice);

}
