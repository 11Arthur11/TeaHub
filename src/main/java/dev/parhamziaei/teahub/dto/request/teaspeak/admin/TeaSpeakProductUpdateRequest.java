package dev.parhamziaei.teahub.dto.request.teaspeak.admin;

import dev.parhamziaei.teahub.valueobject.Money;
import lombok.Data;

import java.time.Duration;

@Data
public class TeaSpeakProductUpdateRequest {

    private Long id;
    private String productName;
    private Integer maxClients;
    private Duration expiration;
    private Money price;

}
