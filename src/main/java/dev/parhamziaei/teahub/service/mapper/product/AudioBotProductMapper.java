package dev.parhamziaei.teahub.service.mapper.product;

import dev.parhamziaei.teahub.dto.response.shop.AbstractProductListResponse;
import dev.parhamziaei.teahub.dto.response.shop.admin.AudioBotProductDetailAdminResponse;
import dev.parhamziaei.teahub.dto.response.shop.admin.AudioBotProductListAdminResponse;
import dev.parhamziaei.teahub.dto.response.shop.user.AudioBotProductListResponse;
import dev.parhamziaei.teahub.entity.jpa.shop.AudioBotProduct;
import dev.parhamziaei.teahub.entity.jpa.shop.BillableProduct;
import dev.parhamziaei.teahub.enums.shop.ProductType;
import dev.parhamziaei.teahub.service.MessageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AudioBotProductMapper implements ProductMapperHandler{

    private final ModelMapper modelMapper;
    private final MessageService messageService;

    @Override
    public ProductType getType() {
        return ProductType.AUDIO_BOT;
    }

    @Override
    public <T extends AbstractProductListResponse, U extends BillableProduct> T enrichListProduct(U product, Class<T> clazz) {
        T response = modelMapper.map(product, clazz);
        response.setPeriod(messageService.get(product.getPeriod()));
        return response;
    }

    @Transactional
    @Override
    public <U extends BillableProduct> AudioBotProductDetailAdminResponse enrichDetailProduct(U product) {
        AudioBotProductDetailAdminResponse response = modelMapper.map(product, AudioBotProductDetailAdminResponse.class);
        response.setPeriod(messageService.get(product.getPeriod()));
        response.setOrderedResources(product.getUserResources().size());
        return response;
    }

    @Override
    public AbstractProductListResponse mapToList(BillableProduct product) {
        AudioBotProduct audioBotProduct = (AudioBotProduct) product;
        return enrichListProduct(audioBotProduct, AudioBotProductListResponse.class);
    }

    @Override
    public AbstractProductListResponse mapToListForAdmin(BillableProduct product) {
        AudioBotProduct audioBotProduct = (AudioBotProduct) product;
        return enrichListProduct(audioBotProduct, AudioBotProductListAdminResponse.class);
    }

    @Override
    @Transactional
    public AudioBotProductDetailAdminResponse mapToDetail(BillableProduct product) {
        AudioBotProduct audioBotProduct = (AudioBotProduct) product;
        return enrichDetailProduct(audioBotProduct);
    }

}
