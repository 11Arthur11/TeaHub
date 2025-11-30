package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.dto.request.teaspeak.admin.TeaSpeakProductRequest;
import dev.parhamziaei.teahub.entity.jpa.shop.Category;
import dev.parhamziaei.teahub.entity.jpa.shop.TeaSpeakProduct;
import dev.parhamziaei.teahub.exception.custom.service.product.CategoryNotFoundException;
import dev.parhamziaei.teahub.repository.jpa.CategoryRepository;
import dev.parhamziaei.teahub.repository.jpa.TeaSpeakProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeaSpeakService {

    private final CategoryRepository categoryRepo;
    private final TeaSpeakProductRepository teaSpeakProductRepo;
    private final QueryInstanceService queryInstanceService;

    public void addNewTeaSpeakProduct(TeaSpeakProductRequest productRequest) {
        Category category = categoryRepo.findBySlug(productRequest.getCategorySlug())
                .orElseThrow(() -> new CategoryNotFoundException("category not found with name " + productRequest.getCategorySlug()));

        //TODO add not enough port exception based on QueryInstance portStep
        TeaSpeakProduct product = TeaSpeakProduct.builder()
                .productName(productRequest.getName())
                .price(productRequest.getPrice())
                .category(category)
                .expiration(productRequest.getExpiration())
                .maxClients(productRequest.getMaxClients())
                .build();

        teaSpeakProductRepo.save(product);
    }

    @Transactional
    public void deployTeaSpeakInstance() {

    }

}
