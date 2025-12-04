package dev.parhamziaei.teahub.dto.request.payment.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GatewayListResponse {

    private Long id;
    private String name;

}
