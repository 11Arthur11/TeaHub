package dev.parhamziaei.teahub.dto.response.shop.admin;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.parhamziaei.teahub.dto.serializer.ExpirationDurationSerializer;
import dev.parhamziaei.teahub.valueobject.Money;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Duration;

@EqualsAndHashCode(callSuper = true)
@Data
public class TeaSpeakProductDetailAdminResponse extends AbstractProductDetailResponse {

    private Integer maxClients;

}
