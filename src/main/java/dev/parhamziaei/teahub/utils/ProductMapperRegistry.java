package dev.parhamziaei.teahub.utils;

import dev.parhamziaei.teahub.dto.response.shop.TeaSpeakProductDTO;
import dev.parhamziaei.teahub.dto.response.shop.user.TeaSpeakProductListResponse;
import dev.parhamziaei.teahub.enums.ProductType;

import java.util.Map;

public class ProductMapperRegistry {

    private final static Map<ProductType, Class<? extends TeaSpeakProductDTO>> listDtoClass = Map.of(
            ProductType.TEA_SPEAK, TeaSpeakProductListResponse.class
    );

    public static Class<? extends TeaSpeakProductDTO> getListDto(ProductType categoryProductType) {
        return listDtoClass.get(categoryProductType);
    }

}
