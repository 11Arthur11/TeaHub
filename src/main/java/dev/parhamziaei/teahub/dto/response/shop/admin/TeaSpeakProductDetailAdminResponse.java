package dev.parhamziaei.teahub.dto.response.shop.admin;

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

    private Duration expiration;

    private Integer orderedResources;

    private Money price;

    private boolean enabled;

}
