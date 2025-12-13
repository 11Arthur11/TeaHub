package dev.parhamziaei.teahub.service.mapper.product;

import dev.parhamziaei.teahub.dto.response.shop.AbstractProductListResponse;
import dev.parhamziaei.teahub.entity.jpa.shop.BillableProduct;
import dev.parhamziaei.teahub.enums.ProductType;

public interface ProductMapperHandler {

    ProductType getType();
    AbstractProductListResponse mapToListForUser(BillableProduct product);
    AbstractProductListResponse mapToListForAdmin(BillableProduct product);

}
