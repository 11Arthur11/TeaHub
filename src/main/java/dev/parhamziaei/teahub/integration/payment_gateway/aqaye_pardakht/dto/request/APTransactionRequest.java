package dev.parhamziaei.teahub.integration.payment_gateway.aqaye_pardakht.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class APTransactionRequest {
    private String pin;
    private String amount;
    private String callback;
    private String invoice_id;
}
