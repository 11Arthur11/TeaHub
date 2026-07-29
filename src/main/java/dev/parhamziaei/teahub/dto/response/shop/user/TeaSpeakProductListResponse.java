package dev.parhamziaei.teahub.dto.response.shop.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TeaSpeakProductListResponse extends AbstractProductListResponse {

    private Integer maxClients;

}
