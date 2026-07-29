package dev.parhamziaei.teahub.dto.response.shop.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TeaSpeakProductListAdminResponse extends AbstractProductListAdminResponse {

    private Integer maxClients;
    private boolean enabled;

}
