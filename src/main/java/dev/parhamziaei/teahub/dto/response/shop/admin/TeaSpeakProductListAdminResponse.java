package dev.parhamziaei.teahub.dto.response.shop.admin;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.parhamziaei.teahub.dto.response.shop.AbstractProductListResponse;
import dev.parhamziaei.teahub.dto.serializer.ExpirationDurationSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Duration;

@EqualsAndHashCode(callSuper = true)
@Data
public class TeaSpeakProductListAdminResponse extends AbstractProductListResponse {

    private Integer maxClients;
    private boolean enabled;

}
