package dev.parhamziaei.teahub.integration.payment_gateway.aghaye_pardakht.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AghayePardakhtTransactionRequest {
    private String pin;
    private String amount;
    private String callback;
    private String invoice_id;
}
