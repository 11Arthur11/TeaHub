package dev.parhamziaei.teahub.dto.response.shop.admin;

import dev.parhamziaei.teahub.dto.response.shop.TeaSpeakProductDTO;
import lombok.Data;

import java.time.Duration;

@Data
public class TeaSpeakProductListAdminResponse implements TeaSpeakProductDTO {

    private Integer maxClients;
    private Duration expiration;
    private boolean enabled;

}
