package dev.parhamziaei.teahub.integration.payment_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TransactionGatewayResponse {
    private String transactionId;
    private String paymentGatewayUrl;
}
