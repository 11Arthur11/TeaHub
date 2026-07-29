package dev.parhamziaei.teahub.service.mapper.product;

import dev.parhamziaei.teahub.dto.response.shop.admin.AbstractProductListAdminResponse;
import dev.parhamziaei.teahub.dto.response.shop.user.AbstractProductListResponse;
import dev.parhamziaei.teahub.dto.response.shop.admin.AudioBotProductDetailAdminResponse;
import dev.parhamziaei.teahub.dto.response.shop.admin.AudioBotProductListAdminResponse;
import dev.parhamziaei.teahub.dto.response.shop.user.AudioBotProductListResponse;
import dev.parhamziaei.teahub.entity.jpa.shop.AudioBotProduct;
import dev.parhamziaei.teahub.entity.jpa.shop.BillableProduct;
import dev.parhamziaei.teahub.enums.shop.ProductType;
import dev.parhamziaei.teahub.repository.jpa.BillableResourceRepository;
import dev.parhamziaei.teahub.service.MessageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AudioBotProductMapper implements ProductMapperHandler{

    private final ModelMapper modelMapper;
    private final BillableResourceRepository billableResourceRepository;

    @Override
    public ProductType getType() {
        return ProductType.AUDIO_BOT;
    }


    @Transactional
    @Override
    public <U extends BillableProduct> AudioBotProductDetailAdminResponse enrichDetailProduct(U product) {
        AudioBotProductDetailAdminResponse response = modelMapper.map(product, AudioBotProductDetailAdminResponse.class);
        response.setOrderedResources(billableResourceRepository.countByProductId(product.getId()));
        return response;
    }

    @Override
    public AbstractProductListResponse mapToList(BillableProduct product) {
        AudioBotProduct audioBotProduct = (AudioBotProduct) product;
        return modelMapper.map(audioBotProduct, AudioBotProductListResponse.class);
    }

    @Override
    public AbstractProductListAdminResponse mapToListForAdmin(BillableProduct product) {
        AudioBotProduct audioBotProduct = (AudioBotProduct) product;
        return modelMapper.map(audioBotProduct, AudioBotProductListAdminResponse.class);
    }

    @Override
    @Transactional
    public AudioBotProductDetailAdminResponse mapToDetail(BillableProduct product) {
        AudioBotProduct audioBotProduct = (AudioBotProduct) product;
        return enrichDetailProduct(audioBotProduct);
    }

}
