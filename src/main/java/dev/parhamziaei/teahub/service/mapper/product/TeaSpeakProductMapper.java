package dev.parhamziaei.teahub.service.mapper.product;

import dev.parhamziaei.teahub.dto.response.shop.admin.AbstractProductListAdminResponse;
import dev.parhamziaei.teahub.dto.response.shop.user.AbstractProductListResponse;
import dev.parhamziaei.teahub.dto.response.shop.admin.TeaSpeakProductDetailAdminResponse;
import dev.parhamziaei.teahub.dto.response.shop.admin.TeaSpeakProductListAdminResponse;
import dev.parhamziaei.teahub.dto.response.shop.user.TeaSpeakProductListResponse;
import dev.parhamziaei.teahub.entity.jpa.shop.BillableProduct;
import dev.parhamziaei.teahub.entity.jpa.shop.TeaSpeakProduct;
import dev.parhamziaei.teahub.enums.shop.ProductType;
import dev.parhamziaei.teahub.repository.jpa.BillableResourceRepository;
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
    private final BillableResourceRepository billableResourceRepository;

    @Override
    public ProductType getType() {
        return ProductType.TEASPEAK;
    }

    @Transactional
    @Override
    public <U extends BillableProduct> TeaSpeakProductDetailAdminResponse enrichDetailProduct(U product) {
        TeaSpeakProductDetailAdminResponse response = modelMapper.map(product, TeaSpeakProductDetailAdminResponse.class);
        response.setOrderedResources(billableResourceRepository.countByProductId(product.getId()));
        return response;
    }

    @Override
    public AbstractProductListResponse mapToList(BillableProduct product) {
        TeaSpeakProduct teaSpeakProduct = (TeaSpeakProduct) product;
        return modelMapper.map(teaSpeakProduct, TeaSpeakProductListResponse.class);
    }

    @Override
    public AbstractProductListAdminResponse mapToListForAdmin(BillableProduct product) {
        TeaSpeakProduct teaSpeakProduct = (TeaSpeakProduct) product;
        return modelMapper.map(teaSpeakProduct, TeaSpeakProductListAdminResponse.class);
    }

    @Override
    @Transactional
    public TeaSpeakProductDetailAdminResponse mapToDetail(BillableProduct product) {
        TeaSpeakProduct teaSpeakProduct = (TeaSpeakProduct) product;
        return enrichDetailProduct(teaSpeakProduct);
    }

}
