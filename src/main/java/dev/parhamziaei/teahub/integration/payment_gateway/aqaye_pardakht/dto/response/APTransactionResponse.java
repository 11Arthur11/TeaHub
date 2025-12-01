package dev.parhamziaei.teahub.integration.payment_gateway.aqaye_pardakht.dto.response;

import lombok.Data;

@Data
public class APTransactionResponse {
    private String status;
    private String transid;
}
