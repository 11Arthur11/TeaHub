package dev.parhamziaei.teahub.dto.request.shop.admin;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.parhamziaei.teahub.dto.deserializer.ExpirationDurationDeserializer;
import dev.parhamziaei.teahub.valueobject.Money;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.Duration;

@EqualsAndHashCode(callSuper = true)
@Data
public class TeaSpeakProductInitRequest extends BaseProductInitRequest {

    private Integer maxClients;
    @Schema(type = "string", description = "Duration in format like 1h, 2d only support days & hour", example = "30d")
    @JsonDeserialize(using = ExpirationDurationDeserializer.class)
    private Duration expiration;


}
