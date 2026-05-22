package dev.parhamziaei.teahub.dto.response.payment.admin;

import dev.parhamziaei.teahub.enums.payment.PaymentGatewayType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APGatewayListAdminResponse {

    private Long id;
    private String name;
    private Boolean active;
    private String merchantId;
    private PaymentGatewayType gatewayType;

}
