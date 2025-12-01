package dev.parhamziaei.teahub.integration.payment_gateway.dto;

import lombok.Data;

@Data
public abstract class CallbackRequest {
    private String status;
    private String transactionId;
    private String invoiceToken;
}
