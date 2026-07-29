package dev.parhamziaei.teahub.dto.response.shop.admin;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.parhamziaei.teahub.dto.serializer.ExpirationDurationSerializer;
import dev.parhamziaei.teahub.valueobject.Money;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Duration;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(name = "TeaSpeakProductDetailAdminResponse")
public class TeaSpeakProductDetailAdminResponse extends AbstractProductDetailResponse {

    private Integer maxClients;

}
