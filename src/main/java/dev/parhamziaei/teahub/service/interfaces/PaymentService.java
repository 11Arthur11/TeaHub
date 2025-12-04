package dev.parhamziaei.teahub.service.interfaces;

import dev.parhamziaei.teahub.integration.payment_gateway.aqaye_pardakht.dto.request.APCallbackRequest;

import java.math.BigDecimal;

public interface PaymentService {
    String createChargeWalletInvoice(Long userId, BigDecimal amount);
    String createPaymentGateway(String invoiceToken, Long gatewayId);
    void verifyAPCallback(APCallbackRequest callbackRequest);
}
