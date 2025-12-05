package dev.parhamziaei.teahub.dto.request.shop.admin;

import dev.parhamziaei.teahub.valueobject.Money;
import lombok.Data;

@Data
public class BaseProductInitRequest {

    private String productName;

    private Long categoryId;

    private Money price;

}
