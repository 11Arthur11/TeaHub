package dev.parhamziaei.teahub.utils;

import dev.parhamziaei.teahub.dto.response.shop.AbstractProductListResponse;
import dev.parhamziaei.teahub.dto.response.shop.user.TeaSpeakProductListResponse;
import dev.parhamziaei.teahub.enums.shop.ProductType;

import java.util.Map;

public class ProductMapperRegistry {

    private final static Map<ProductType, Class<? extends AbstractProductListResponse>> listDtoClass = Map.of(
            ProductType.TEASPEAK_PRODUCT, TeaSpeakProductListResponse.class
    );

    public static Class<? extends AbstractProductListResponse> getListDto(ProductType categoryProductType) {
        return listDtoClass.get(categoryProductType);
    }

}
