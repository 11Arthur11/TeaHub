package dev.parhamziaei.teahub.dto.response.shop.admin;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.parhamziaei.teahub.dto.response.shop.TeaSpeakProductDTO;
import dev.parhamziaei.teahub.dto.serializer.ExpirationDurationSerializer;
import lombok.Data;

import java.time.Duration;

@Data
public class TeaSpeakProductListAdminResponse implements TeaSpeakProductDTO {

    private Integer maxClients;
    @JsonSerialize(using = ExpirationDurationSerializer.class)
    private Duration expiration;
    private boolean enabled;

}
