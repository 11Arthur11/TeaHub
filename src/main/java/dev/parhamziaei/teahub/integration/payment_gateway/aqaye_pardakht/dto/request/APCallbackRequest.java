package dev.parhamziaei.teahub.integration.payment_gateway.aqaye_pardakht.dto.request;

import dev.parhamziaei.teahub.integration.payment_gateway.dto.CallbackRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class APCallbackRequest extends CallbackRequest {

    private String cardnumber;
    private String tracking_number;
    private String bank;

}
