package dev.parhamziaei.teahub.dto.request.teaspeak.admin;

import dev.parhamziaei.teahub.valueobject.Money;
import lombok.Data;

import java.time.Duration;

@Data
public class TeaSpeakProductRequest {

    private String name;
    private Duration expiration;
    private Integer maxClients;
    private Money price;
    private String categorySlug;

}
