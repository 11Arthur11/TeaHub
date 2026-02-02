package dev.parhamziaei.teahub.service.product.registry;

import dev.parhamziaei.teahub.dto.request.shop.admin.AbstractProductEditRequest;
import dev.parhamziaei.teahub.dto.request.shop.admin.AbstractProductInitRequest;
import dev.parhamziaei.teahub.dto.request.shop.admin.AudioBotProductInitRequest;
import dev.parhamziaei.teahub.entity.jpa.shop.AudioBotProduct;
import dev.parhamziaei.teahub.entity.jpa.shop.Category;
import dev.parhamziaei.teahub.enums.shop.ResourceType;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.repository.jpa.AudioBotProductRepository;
import dev.parhamziaei.teahub.repository.jpa.CategoryRepository;
import dev.parhamziaei.teahub.valueobject.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AudioBotProductRegistry implements ProductRegistryHandler {

    private final CategoryRepository categoryRepo;
    private final AudioBotProductRepository audioBotProductRepo;

    @Override
    public ResourceType getType() {
        return ResourceType.AUDIO_BOT;
    }

    @Override
    public void initProduct(AbstractProductInitRequest request) {
        AudioBotProductInitRequest initRequest = (AudioBotProductInitRequest) request;

        Category category = categoryRepo.findById(initRequest.getCategoryId())
                .orElseThrow(NoSuchEntityException::new);

        Money price = new Money(initRequest.getPrice());
        AudioBotProduct product = AudioBotProduct.builder()
                .productName(initRequest.getProductName())
                .price(price)
                .enabled(initRequest.isEnabled())
                .expiration(initRequest.getProductPeriod().duration())
                .period(initRequest.getProductPeriod())
                .providerNodeId(initRequest.getProviderNodeId())
                .build();

        category.addProduct(product);
        audioBotProductRepo.save(product);
    }

    @Override
    public void editProduct(Long productId, AbstractProductEditRequest request) {
        //TODO
    }

}
