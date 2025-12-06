package dev.parhamziaei.teahub.dto.response.shop.user;

import dev.parhamziaei.teahub.dto.response.shop.TeaSpeakProductDTO;
import dev.parhamziaei.teahub.valueobject.Money;
import lombok.Data;

import java.time.Duration;

@Data
public class TeaSpeakProductListResponse implements TeaSpeakProductDTO {

    private Long id;
    private String productName;
    private Money price;
    private Integer maxClients;
    private Duration expiration;

}
