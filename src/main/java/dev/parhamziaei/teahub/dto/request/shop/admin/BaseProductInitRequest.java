package dev.parhamziaei.teahub.dto.request.shop.admin;

import dev.parhamziaei.teahub.valueobject.Money;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BaseProductInitRequest {

    private String productName;

    private Long categoryId;

    private BigDecimal price;

    private boolean enabled;

}
