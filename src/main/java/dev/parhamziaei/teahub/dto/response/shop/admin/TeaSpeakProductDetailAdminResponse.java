package dev.parhamziaei.teahub.dto.response.shop.admin;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.parhamziaei.teahub.dto.serializer.ExpirationDurationSerializer;
import dev.parhamziaei.teahub.valueobject.Money;
import lombok.Data;

import java.time.Duration;

@Data
public class TeaSpeakProductDetailAdminResponse {

    private Long id;

    private String categoryName;

    private String categorySlug;

    private String productName;

    private Integer maxClients;

    @JsonSerialize(using = ExpirationDurationSerializer.class)
    private Duration expiration;

    private Integer orderedResources;

    private Money price;

    private boolean enabled;

}
