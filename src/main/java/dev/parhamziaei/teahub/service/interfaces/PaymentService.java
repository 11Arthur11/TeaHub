package dev.parhamziaei.teahub.service.interfaces;

import dev.parhamziaei.teahub.enums.PaymentGatewayType;

import java.math.BigDecimal;

public interface PaymentService {
    String createChargeWalletInvoice(Long userId, BigDecimal amount);
    String createPaymentGateway(String invoiceToken, Long gatewayId);
}
