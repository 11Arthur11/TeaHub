package dev.parhamziaei.teahub.service.implement;

import dev.parhamziaei.teahub.dto.request.shop.admin.TeaSpeakProductInitRequest;
import dev.parhamziaei.teahub.dto.request.shop.admin.TeaSpeakProductUpdateRequest;
import dev.parhamziaei.teahub.dto.response.shop.TeaSpeakProductDTO;
import dev.parhamziaei.teahub.dto.response.shop.admin.TeaSpeakProductDetailAdminResponse;
import dev.parhamziaei.teahub.dto.response.shop.admin.TeaSpeakProductListAdminResponse;
import dev.parhamziaei.teahub.entity.jpa.shop.BaseProduct;
import dev.parhamziaei.teahub.entity.jpa.shop.Category;
import dev.parhamziaei.teahub.entity.jpa.shop.TeaSpeakProduct;
import dev.parhamziaei.teahub.enums.CategoryProductType;
import dev.parhamziaei.teahub.exception.custom.global.EntityInUseException;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchDataException;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.repository.jpa.CategoryRepository;
import dev.parhamziaei.teahub.repository.jpa.ProductRepository;
import dev.parhamziaei.teahub.repository.jpa.TeaSpeakProductRepository;
import dev.parhamziaei.teahub.service.interfaces.ProductService;
import dev.parhamziaei.teahub.utils.ProductMapperRegistry;
import dev.parhamziaei.teahub.valueobject.Money;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
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

        Money price = new Money(initRequest.getPrice());
        TeaSpeakProduct product = TeaSpeakProduct.builder()
                .productName(initRequest.getProductName())
                .price(price)
                .maxClients(initRequest.getMaxClients())
                .expiration(initRequest.getExpiration())
                .build();

        if (category.getProductType() == CategoryProductType.EMPTY)
            category.setProductType(CategoryProductType.TEA_SPEAK);

        category.appendProduct(product);
        teaSpeakProductRepo.save(product);
    }

    @Override
    @Transactional
    public void updateTeaSpeakProduct(TeaSpeakProductUpdateRequest updateRequest) {
        TeaSpeakProduct product = teaSpeakProductRepo.findById(updateRequest.getId())
                        .orElseThrow(NoSuchEntityException::new);
        modelMapper.map(updateRequest, product);
        if (updateRequest.getCategoryId() != null) {
            Long oldCategoryId = product.getCategory().getId();
            Category newCategory = categoryRepo.findById(updateRequest.getCategoryId())
                    .orElseThrow(NoSuchEntityException::new);
            product.setCategory(newCategory);
            updateCategoryProductsType(oldCategoryId);
        }
        teaSpeakProductRepo.save(product);
    }

    @Override
    @Transactional
    public void updateCategoryProductsType(Long oldCategoryId) {
        Category category = categoryRepo.findById(oldCategoryId)
                .orElseThrow(NoSuchEntityException::new);
        Hibernate.initialize(category.getProducts());
        if (category.getProducts().isEmpty())
            category.setProductType(CategoryProductType.EMPTY);
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

        if (!category.isActive())
            throw new NoSuchDataException();

        List<? extends TeaSpeakProductDTO> mappedResponse = category.getProducts()
                .stream()
                .map(baseProduct -> {
                    Class<? extends TeaSpeakProductDTO> dtoClass = ProductMapperRegistry.getListDto(category.getProductType());
                    return modelMapper.map(baseProduct, dtoClass);
                })
                .toList();

        if (mappedResponse.isEmpty())
            throw new NoSuchDataException();

        return mappedResponse;
    }
}
