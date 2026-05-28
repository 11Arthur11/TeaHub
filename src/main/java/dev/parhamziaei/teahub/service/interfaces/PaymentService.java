package dev.parhamziaei.teahub.service.interfaces;

import dev.parhamziaei.teahub.entity.jpa.payment.invoice.Invoice;
import dev.parhamziaei.teahub.integration.payment_gateway.aqaye_pardakht.dto.request.APCallbackRequest;
import dev.parhamziaei.teahub.integration.payment_gateway.dto.CallbackRequest;

import java.math.BigDecimal;

public interface PaymentService {
    String createPaymentGatewayUri(Long userId, String invoiceToken, Long gatewayId);
    void verifyAPCallback(APCallbackRequest callbackRequest);
    <T extends CallbackRequest> void savePaymentTransaction(T callbackRequest, Invoice invoice, String gatewayName);
}
