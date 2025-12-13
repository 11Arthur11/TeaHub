package dev.parhamziaei.teahub.service.mapper.product;

import dev.parhamziaei.teahub.dto.response.shop.AbstractProductListResponse;
import dev.parhamziaei.teahub.dto.response.shop.admin.TeaSpeakProductListAdminResponse;
import dev.parhamziaei.teahub.dto.response.shop.user.TeaSpeakProductListResponse;
import dev.parhamziaei.teahub.entity.jpa.shop.BillableProduct;
import dev.parhamziaei.teahub.enums.ProductType;
import dev.parhamziaei.teahub.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeaSpeakProductMapper implements ProductMapperHandler {

    private final ModelMapper modelMapper;
    private final MessageService messageService;

    @Override
    public ProductType getType() {
        return ProductType.TEASPEAK_PRODUCT;
    }

    public <T extends AbstractProductListResponse, U extends BillableProduct> T enrichProduct(U product, Class<T> clazz) {
        T response = modelMapper.map(product, clazz);
        response.setPeriod(messageService.get(product.getPeriod()));
        return response;
    }

    @Override
    public TeaSpeakProductListResponse mapToListForUser(BillableProduct product) {
        return enrichProduct(product, TeaSpeakProductListResponse.class);
    }

    @Override
    public TeaSpeakProductListAdminResponse mapToListForAdmin(BillableProduct product) {
        return null;
    }
}
