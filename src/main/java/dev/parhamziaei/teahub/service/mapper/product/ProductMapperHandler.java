package dev.parhamziaei.teahub.service.mapper.product;

import dev.parhamziaei.teahub.dto.response.shop.admin.AbstractProductListAdminResponse;
import dev.parhamziaei.teahub.dto.response.shop.user.AbstractProductListResponse;
import dev.parhamziaei.teahub.dto.response.shop.admin.AbstractProductDetailResponse;
import dev.parhamziaei.teahub.entity.jpa.shop.BillableProduct;
import dev.parhamziaei.teahub.enums.shop.ProductType;

public interface ProductMapperHandler {

    ProductType getType();
    AbstractProductListResponse mapToList(BillableProduct product);
    AbstractProductListAdminResponse mapToListForAdmin(BillableProduct product);
    AbstractProductDetailResponse mapToDetail(BillableProduct product);
    <U extends BillableProduct> AbstractProductDetailResponse enrichDetailProduct(U product);

}
