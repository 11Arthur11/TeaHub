package dev.parhamziaei.teahub.service.product.registry;

import dev.parhamziaei.teahub.dto.request.shop.admin.*;
import dev.parhamziaei.teahub.entity.jpa.shop.AudioBotProduct;
import dev.parhamziaei.teahub.entity.jpa.shop.Category;
import dev.parhamziaei.teahub.entity.jpa.shop.TeaSpeakProduct;
import dev.parhamziaei.teahub.enums.shop.ResourceType;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.repository.jpa.AudioBotProductRepository;
import dev.parhamziaei.teahub.repository.jpa.CategoryRepository;
import dev.parhamziaei.teahub.service.mapper.product.AudioBotProductMapStruct;
import dev.parhamziaei.teahub.valueobject.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AudioBotProductRegistry implements ProductRegistryHandler {

    private final CategoryRepository categoryRepo;
    private final AudioBotProductRepository audioBotProductRepo;
    private final AudioBotProductMapStruct audioBotProductMapStruct;

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
                .presentation(initRequest.getPresentation())
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
        AudioBotProductEditRequest editRequest = (AudioBotProductEditRequest) request;

        AudioBotProduct product = audioBotProductRepo.findById(productId)
                .orElseThrow(NoSuchEntityException::new);

        if (editRequest.getCategoryId() != null && !editRequest.getCategoryId().equals(product.getCategory().getId())) {
            Category category = categoryRepo.findById(editRequest.getCategoryId())
                    .orElseThrow(NoSuchEntityException::new);

            product.setCategory(category);
        }

        audioBotProductMapStruct.updateEntity(editRequest, product);
        if (editRequest.getPresentation() != null)
            product.setPresentation(editRequest.getPresentation());
        audioBotProductRepo.save(product);
    }

}
