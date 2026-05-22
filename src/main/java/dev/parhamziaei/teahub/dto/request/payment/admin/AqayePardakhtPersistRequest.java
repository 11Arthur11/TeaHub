package dev.parhamziaei.teahub.dto.request.payment.admin;

import dev.parhamziaei.teahub.dto.request.GatewayRequestSubType;
import dev.parhamziaei.teahub.enums.payment.PaymentGatewayType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@GatewayRequestSubType(PaymentGatewayType.AQAYE_PARDAKHT)
public class AqayePardakhtPersistRequest extends GatewayPersistRequest {

    private String merchantId;

}
