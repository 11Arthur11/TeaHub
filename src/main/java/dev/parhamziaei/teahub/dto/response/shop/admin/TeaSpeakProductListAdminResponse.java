package dev.parhamziaei.teahub.dto.response.shop.admin;

import dev.parhamziaei.teahub.entity.jpa.shop.Category;
import dev.parhamziaei.teahub.valueobject.Money;
import lombok.Data;

import java.time.Duration;

@Data
public class TeaSpeakProductListAdminResponse {

    private Long id;
    private String productName;
    private Integer maxClients;
    private Duration expiration;
    private Money price;
    private boolean enabled;

}
