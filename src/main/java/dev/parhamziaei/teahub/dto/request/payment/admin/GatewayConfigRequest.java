package dev.parhamziaei.teahub.dto.request.payment.admin;

import dev.parhamziaei.teahub.enums.PaymentGatewayType;
import lombok.Data;

@Data
public class GatewayConfigRequest {

    private String name;
    private String merchantId;
    private PaymentGatewayType type;

}
