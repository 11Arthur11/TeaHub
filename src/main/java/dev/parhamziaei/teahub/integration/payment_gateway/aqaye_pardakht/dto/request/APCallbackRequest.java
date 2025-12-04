package dev.parhamziaei.teahub.integration.payment_gateway.aqaye_pardakht.dto.request;

import dev.parhamziaei.teahub.integration.payment_gateway.dto.CallbackRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class APCallbackRequest extends CallbackRequest {

    private String cardnumber;
    private String tracking_number;
    private String bank;

}
