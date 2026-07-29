package dev.parhamziaei.teahub.service.product.registry;

import dev.parhamziaei.teahub.dto.request.shop.admin.AbstractProductEditRequest;
import dev.parhamziaei.teahub.dto.request.shop.admin.AbstractProductInitRequest;
import dev.parhamziaei.teahub.dto.request.shop.admin.TeaSpeakProductEditRequest;
import dev.parhamziaei.teahub.dto.request.shop.admin.TeaSpeakProductInitRequest;
import dev.parhamziaei.teahub.entity.jpa.shop.Category;
import dev.parhamziaei.teahub.entity.jpa.shop.TeaSpeakProduct;
import dev.parhamziaei.teahub.enums.shop.ProductType;
import dev.parhamziaei.teahub.enums.shop.ResourceType;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.repository.jpa.CategoryRepository;
import dev.parhamziaei.teahub.repository.jpa.TeaSpeakProductRepository;
import dev.parhamziaei.teahub.service.mapper.product.TeaSpeakProductMapStruct;
import dev.parhamziaei.teahub.valueobject.Money;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeaSpeakProductRegistry implements ProductRegistryHandler {

    private final CategoryRepository categoryRepo;
    private final TeaSpeakProductRepository teaSpeakProductRepo;
    private final TeaSpeakProductMapStruct teaSpeakProductMapStruct;

    @Override
    public ResourceType getType() {
        return ResourceType.TEASPEAK;
    }

    @Override
    public void initProduct(AbstractProductInitRequest request) {
        TeaSpeakProductInitRequest initRequest = (TeaSpeakProductInitRequest) request;

        Category category = categoryRepo.findById(initRequest.getCategoryId())
                .orElseThrow(NoSuchEntityException::new);

        Money price = new Money(initRequest.getPrice());
        TeaSpeakProduct product = TeaSpeakProduct.builder()
                .productName(initRequest.getProductName())
                .presentation(request.getPresentation())
                .price(price)
                .enabled(initRequest.isEnabled())
                .maxClients(initRequest.getMaxClients())
                .expiration(initRequest.getProductPeriod().duration())
                .period(initRequest.getProductPeriod())
                .build();

        category.addProduct(product);
        teaSpeakProductRepo.save(product);
    }

    @Override
    @Transactional
    public void editProduct(Long productId, AbstractProductEditRequest request) {
        TeaSpeakProductEditRequest editRequest = (TeaSpeakProductEditRequest) request;

        TeaSpeakProduct product = teaSpeakProductRepo.findById(productId)
                        .orElseThrow(NoSuchEntityException::new);

        if (editRequest.getCategoryId() != null && !editRequest.getCategoryId().equals(product.getCategory().getId())) {
            Category category = categoryRepo.findById(editRequest.getCategoryId())
                    .orElseThrow(NoSuchEntityException::new);

            product.setCategory(category);
        }

        teaSpeakProductMapStruct.updateEntity(editRequest, product);
        if (editRequest.getPresentation() != null)
            product.setPresentation(editRequest.getPresentation());
        teaSpeakProductRepo.save(product);
    }

}
