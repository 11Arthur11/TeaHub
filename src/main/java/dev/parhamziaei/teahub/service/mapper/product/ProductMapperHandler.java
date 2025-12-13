package dev.parhamziaei.teahub.service.mapper.product;

import dev.parhamziaei.teahub.dto.response.shop.AbstractProductListResponse;
import dev.parhamziaei.teahub.dto.response.shop.admin.AbstractProductDetailResponse;
import dev.parhamziaei.teahub.entity.jpa.shop.BillableProduct;
import dev.parhamziaei.teahub.enums.ProductType;

public interface ProductMapperHandler {

    ProductType getType();
    <T extends AbstractProductListResponse> AbstractProductListResponse mapToList(BillableProduct product, Class<T> clazz);
    <T extends AbstractProductDetailResponse> AbstractProductDetailResponse mapToDetail(BillableProduct product, Class<T> clazz);
}
