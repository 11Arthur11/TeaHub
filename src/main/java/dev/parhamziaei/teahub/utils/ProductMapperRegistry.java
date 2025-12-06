package dev.parhamziaei.teahub.utils;

import dev.parhamziaei.teahub.dto.response.shop.TeaSpeakProductDTO;
import dev.parhamziaei.teahub.dto.response.shop.user.TeaSpeakProductListResponse;
import dev.parhamziaei.teahub.enums.CategoryProductType;

import java.util.Map;

public class ProductMapperRegistry {

    private final static Map<CategoryProductType, Class<? extends TeaSpeakProductDTO>> listDtoClass = Map.of(
            CategoryProductType.TEA_SPEAK, TeaSpeakProductListResponse.class
    );

    public static Class<? extends TeaSpeakProductDTO> getListDto(CategoryProductType categoryProductType) {
        return listDtoClass.get(categoryProductType);
    }

}
