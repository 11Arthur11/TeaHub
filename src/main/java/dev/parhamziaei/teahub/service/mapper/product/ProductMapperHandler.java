package dev.parhamziaei.teahub.service.mapper.product;

import dev.parhamziaei.teahub.dto.response.shop.AbstractProductListResponse;
import dev.parhamziaei.teahub.dto.response.shop.admin.TeaSpeakProductDetailAdminResponse;
import dev.parhamziaei.teahub.entity.jpa.shop.BillableProduct;
import dev.parhamziaei.teahub.enums.shop.ProductType;

public interface ProductMapperHandler {

    ProductType getType();
    AbstractProductListResponse mapToList(BillableProduct product);
    AbstractProductListResponse mapToListForAdmin(BillableProduct product);
    TeaSpeakProductDetailAdminResponse mapToDetail(BillableProduct product);
}
