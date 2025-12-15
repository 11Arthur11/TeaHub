package dev.parhamziaei.teahub.dto.response.shop.admin;

import dev.parhamziaei.teahub.enums.shop.ProductType;
import lombok.Data;

@Data
public class CategoryListAdminResponse {

    private Long id;
    private String name;
    private boolean active;
    private ProductType productType;
    private String description;
    private String slug;

}
