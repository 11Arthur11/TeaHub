package dev.parhamziaei.teahub.integration.payment_gateway.aqaye_pardakht.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class APVerifyRequest {
    private String pin;
    private String transid;
    private Integer amount;
}
