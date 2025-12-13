package dev.parhamziaei.teahub.dto.response.shop;

import dev.parhamziaei.teahub.valueobject.Money;
import lombok.Data;

@Data
public abstract class AbstractProductListResponse {
    private Long id;
    private String productName;
    private Money price;
    private String period;
}
