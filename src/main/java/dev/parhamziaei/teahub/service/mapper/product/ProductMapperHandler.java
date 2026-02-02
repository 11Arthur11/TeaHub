package dev.parhamziaei.teahub.service.mapper.product;

import dev.parhamziaei.teahub.dto.response.shop.AbstractProductListResponse;
import dev.parhamziaei.teahub.dto.response.shop.admin.AbstractProductDetailResponse;
import dev.parhamziaei.teahub.dto.response.shop.admin.TeaSpeakProductDetailAdminResponse;
import dev.parhamziaei.teahub.entity.jpa.shop.BillableProduct;
import dev.parhamziaei.teahub.enums.shop.ProductType;

public interface ProductMapperHandler {

    ProductType getType();
    AbstractProductListResponse mapToList(BillableProduct product);
    AbstractProductListResponse mapToListForAdmin(BillableProduct product);
    AbstractProductDetailResponse mapToDetail(BillableProduct product);
    <T extends AbstractProductListResponse, U extends BillableProduct> T enrichListProduct(U product, Class<T> clazz);
    <U extends BillableProduct> AbstractProductDetailResponse enrichDetailProduct(U product);

}
