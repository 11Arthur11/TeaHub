package dev.parhamziaei.teahub.service.mapper.product;

import dev.parhamziaei.teahub.dto.response.shop.AbstractProductListResponse;
import dev.parhamziaei.teahub.dto.response.shop.admin.TeaSpeakProductDetailAdminResponse;
import dev.parhamziaei.teahub.dto.response.shop.admin.TeaSpeakProductListAdminResponse;
import dev.parhamziaei.teahub.dto.response.shop.user.TeaSpeakProductListResponse;
import dev.parhamziaei.teahub.entity.jpa.shop.BillableProduct;
import dev.parhamziaei.teahub.entity.jpa.shop.TeaSpeakProduct;
import dev.parhamziaei.teahub.enums.shop.ProductType;
import dev.parhamziaei.teahub.service.MessageService;
import jakarta.transaction.Transactional;
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
        return ProductType.TEASPEAK;
    }

    @Override
    public <T extends AbstractProductListResponse, U extends BillableProduct> T enrichListProduct(U product, Class<T> clazz) {
        T response = modelMapper.map(product, clazz);
        response.setPeriod(messageService.get(product.getPeriod()));
        return response;
    }

    @Transactional
    @Override
    public <U extends BillableProduct> TeaSpeakProductDetailAdminResponse enrichDetailProduct(U product) {
        TeaSpeakProductDetailAdminResponse response = modelMapper.map(product, TeaSpeakProductDetailAdminResponse.class);
        response.setPeriod(messageService.get(product.getPeriod()));
        response.setOrderedResources(product.getUserResources().size());
        return response;
    }

    @Override
    public AbstractProductListResponse mapToList(BillableProduct product) {
        TeaSpeakProduct teaSpeakProduct = (TeaSpeakProduct) product;
        return enrichListProduct(teaSpeakProduct, TeaSpeakProductListResponse.class);
    }

    @Override
    public AbstractProductListResponse mapToListForAdmin(BillableProduct product) {
        TeaSpeakProduct teaSpeakProduct = (TeaSpeakProduct) product;
        return enrichListProduct(teaSpeakProduct, TeaSpeakProductListAdminResponse.class);
    }

    @Override
    @Transactional
    public TeaSpeakProductDetailAdminResponse mapToDetail(BillableProduct product) {
        TeaSpeakProduct teaSpeakProduct = (TeaSpeakProduct) product;
        return enrichDetailProduct(teaSpeakProduct);
    }

}
