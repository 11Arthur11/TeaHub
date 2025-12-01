package dev.parhamziaei.teahub.integration.payment_gateway.aqaye_pardakht.dto.response;

import lombok.Data;
import lombok.Getter;

@Data
public class APVerifyResponse {

    private String status;
    private String code;

}
