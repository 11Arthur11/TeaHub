package dev.parhamziaei.teahub.dto.response.shop.admin;

import dev.parhamziaei.teahub.enums.shop.ProductType;
import dev.parhamziaei.teahub.valueobject.Money;
import dev.parhamziaei.teahub.valueobject.ProductPresentation;
import lombok.Data;

@Data
public abstract class AbstractProductListAdminResponse {
    private Long id;
    private String productName;
    private Money price;
    private String period;
    private ProductType productType;
    private String categoryName;
    private String categorySlug;
    private Long orderedResources;
}
