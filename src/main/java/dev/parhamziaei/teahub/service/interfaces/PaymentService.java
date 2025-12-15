package dev.parhamziaei.teahub.service.interfaces;

import dev.parhamziaei.teahub.entity.jpa.payment.Invoice;
import dev.parhamziaei.teahub.enums.payment.PaymentGatewayType;
import dev.parhamziaei.teahub.integration.payment_gateway.aqaye_pardakht.dto.request.APCallbackRequest;
import dev.parhamziaei.teahub.integration.payment_gateway.dto.CallbackRequest;

import java.math.BigDecimal;

public interface PaymentService {
    String createChargeWalletInvoice(Long userId, BigDecimal amount);
    String createPaymentGateway(String invoiceToken, Long gatewayId);
    void verifyAPCallback(APCallbackRequest callbackRequest);
    <T extends CallbackRequest> void savePaymentTransaction(T callbackRequest, Invoice invoice, PaymentGatewayType gatewayType);
}
