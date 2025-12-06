package dev.parhamziaei.teahub.service.implement;

import dev.parhamziaei.teahub.dto.request.teaspeak.admin.TeaSpeakProductInitRequest;
import dev.parhamziaei.teahub.dto.request.teaspeak.admin.TeaSpeakProductUpdateRequest;
import dev.parhamziaei.teahub.dto.response.shop.TeaSpeakProductDTO;
import dev.parhamziaei.teahub.dto.response.shop.admin.TeaSpeakProductDetailAdminResponse;
import dev.parhamziaei.teahub.dto.response.shop.admin.TeaSpeakProductListAdminResponse;
import dev.parhamziaei.teahub.entity.jpa.shop.BaseProduct;
import dev.parhamziaei.teahub.entity.jpa.shop.Category;
import dev.parhamziaei.teahub.entity.jpa.shop.TeaSpeakProduct;
import dev.parhamziaei.teahub.enums.CategoryProductType;
import dev.parhamziaei.teahub.exception.custom.global.EntityInUseException;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.repository.jpa.CategoryRepository;
import dev.parhamziaei.teahub.repository.jpa.ProductRepository;
import dev.parhamziaei.teahub.repository.jpa.TeaSpeakProductRepository;
import dev.parhamziaei.teahub.service.interfaces.ProductService;
import dev.parhamziaei.teahub.utils.ProductMapperRegistry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final TeaSpeakProductRepository teaSpeakProductRepo;
    private final CategoryRepository categoryRepo;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    @Override
    public List<TeaSpeakProductListAdminResponse> getAllTeaSpeakProducts() {
        List<TeaSpeakProductListAdminResponse> responses = teaSpeakProductRepo.findAll()
                .stream()
                .map(teaSpeakProduct -> modelMapper.map(teaSpeakProduct, TeaSpeakProductListAdminResponse.class))
                .toList();

        if (responses.isEmpty())
            throw new NoSuchEntityException();
        return responses;
    }

    @Override
    public TeaSpeakProductDetailAdminResponse getTeaSpeakProductById(Long productId) {
        TeaSpeakProduct product = teaSpeakProductRepo.findById(productId)
                .orElseThrow(() -> new NoSuchEntityException("Product not found"));
        TeaSpeakProductDetailAdminResponse response = modelMapper.map(product, TeaSpeakProductDetailAdminResponse.class);
        response.setCategoryName(product.getCategory().getName());
        response.setCategorySlug(product.getCategory().getSlug());
        response.setOrderedResources(product.getUserResources().size());
        return response;
    }

    @Override
    @Transactional
    public void initTeaSpeakProduct(TeaSpeakProductInitRequest initRequest) {
        Category category = categoryRepo.findById(initRequest.getCategoryId())
                .orElseThrow(NoSuchEntityException::new);

        TeaSpeakProduct product = TeaSpeakProduct.builder()
                .productName(initRequest.getProductName())
                .price(initRequest.getPrice())
                .maxClients(initRequest.getMaxClients())
                .expiration(initRequest.getExpiration())
                .build();

        if (category.getProductType() == null)
            category.setProductType(CategoryProductType.TEA_SPEAK);

        category.appendProduct(product);
        teaSpeakProductRepo.save(product);
    }

    @Override
    public void updateTeaSpeakProduct(TeaSpeakProductUpdateRequest updateRequest) {
        TeaSpeakProduct updatedProduct = modelMapper.map(updateRequest, TeaSpeakProduct.class);
        teaSpeakProductRepo.save(updatedProduct);
    }

    @Override
    @Transactional
    public void removeTeaSpeakProduct(Long productId) {
        TeaSpeakProduct product = teaSpeakProductRepo.findById(productId)
                .orElseThrow(NoSuchEntityException::new);
        if (product.getUserResources().isEmpty())
            teaSpeakProductRepo.delete(product);
        else
            throw new EntityInUseException("Product has user resources children");
    }

    @Override
    public void changeEnabled(Long productId, boolean enabled) {
        BaseProduct product = productRepository.findById(productId)
                .orElseThrow(NoSuchEntityException::new);

        product.setEnabled(enabled);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public List<? extends TeaSpeakProductDTO> getAvailableProductsByCategorySlug(String categorySlug) {
        Category category = categoryRepo.findBySlug(categorySlug)
                .orElseThrow(NoSuchEntityException::new);

        return category.getProducts()
                .stream()
                .map(baseProduct -> {
                    Class<? extends TeaSpeakProductDTO> dtoClass = ProductMapperRegistry.getListDto(category.getProductType());
                    return modelMapper.map(baseProduct, dtoClass);
                })
                .toList();
    }
}
