package dev.parhamziaei.teahub.service.implement;

import dev.parhamziaei.teahub.dto.request.shop.admin.TeaSpeakProductRequest;
import dev.parhamziaei.teahub.dto.response.shop.AbstractProductListResponse;
import dev.parhamziaei.teahub.dto.response.shop.admin.AbstractProductDetailResponse;
import dev.parhamziaei.teahub.dto.response.shop.admin.TeaSpeakProductDetailAdminResponse;
import dev.parhamziaei.teahub.dto.response.shop.admin.TeaSpeakProductListAdminResponse;
import dev.parhamziaei.teahub.entity.jpa.shop.BillableProduct;
import dev.parhamziaei.teahub.entity.jpa.shop.Category;
import dev.parhamziaei.teahub.entity.jpa.shop.TeaSpeakProduct;
import dev.parhamziaei.teahub.exception.custom.global.EntityInUseException;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchDataException;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.repository.jpa.BillableProductRepository;
import dev.parhamziaei.teahub.repository.jpa.CategoryRepository;
import dev.parhamziaei.teahub.repository.jpa.ProductRepository;
import dev.parhamziaei.teahub.repository.jpa.TeaSpeakProductRepository;
import dev.parhamziaei.teahub.service.interfaces.ProductService;
import dev.parhamziaei.teahub.service.mapper.product.ProductMapperFactory;
import dev.parhamziaei.teahub.utils.ProductMapperRegistry;
import dev.parhamziaei.teahub.valueobject.Money;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final TeaSpeakProductRepository teaSpeakProductRepo;
    private final BillableProductRepository billableProductRepo;
    private final CategoryRepository categoryRepo;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final ProductMapperFactory productMapperFactory;

    @Override
    public List<AbstractProductListResponse> getAllProducts() {
        List<AbstractProductListResponse> responses = billableProductRepo.findAll()
                .stream()
                .map(p -> productMapperFactory.getMapper(p.getProductType()).mapToListForAdmin(p))
                .toList();

        if (responses.isEmpty())
            throw new NoSuchDataException();
        return responses;
    }

    @Override
    public AbstractProductDetailResponse getProduct(Long productId) {
        BillableProduct product = billableProductRepo.findById(productId)
                .orElseThrow(NoSuchEntityException::new);
        return productMapperFactory.getMapper(product.getProductType()).mapToDetail(product);
    }

    @Override
    @Transactional
    public void initTeaSpeakProduct(TeaSpeakProductRequest initRequest) {
        Category category = categoryRepo.findById(initRequest.getCategoryId())
                .orElseThrow(NoSuchEntityException::new);

        Money price = new Money(initRequest.getPrice());
        TeaSpeakProduct product = TeaSpeakProduct.builder()
                .productName(initRequest.getProductName())
                .price(price)
                .enabled(initRequest.isEnabled())
                .maxClients(initRequest.getMaxClients())
                .expiration(initRequest.getProductPeriod().duration())
                .period(initRequest.getProductPeriod())
                .build();

        category.addProduct(product);
        teaSpeakProductRepo.save(product);
    }

//!    @Override
//    @Transactional
//    public void updateTeaSpeakProduct(TeaSpeakProductUpdateRequest updateRequest) {
//        TeaSpeakProduct product = teaSpeakProductRepo.findById(updateRequest.getId())
//                        .orElseThrow(NoSuchEntityException::new);
//        modelMapper.map(updateRequest, product);
//        if (updateRequest.getCategoryId() != null) {
//            Long oldCategoryId = product.getCategory().getId();
//            Category newCategory = categoryRepo.findById(updateRequest.getCategoryId())
//                    .orElseThrow(NoSuchEntityException::new);
//            product.setCategory(newCategory);
//            updateCategoryProductsType(oldCategoryId);
//        }
//        teaSpeakProductRepo.save(product);
//    }

// ?    @Override
//    @Transactional
//    public void updateCategoryProductsType(Long oldCategoryId) {
//        Category category = categoryRepo.findById(oldCategoryId)
//                .orElseThrow(NoSuchEntityException::new);
//        Hibernate.initialize(category.getProducts());
//        if (category.getProducts().isEmpty())
//            category.setProductType(ProductType.EMPTY);
//    }

    @Override
    @Transactional
    public void removeProduct(Long productId) {
        BillableProduct product = billableProductRepo.findById(productId)
                .orElseThrow(NoSuchEntityException::new);
        if (product.getUserResources().isEmpty())
            billableProductRepo.delete(product);
        else
            throw new EntityInUseException("Product has user resources children");
    }

    @Override
    public void changeEnabled(Long productId, boolean enabled) {
        BillableProduct product = productRepository.findById(productId)
                .orElseThrow(NoSuchEntityException::new);

        product.setEnabled(enabled);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public List<AbstractProductListResponse> getAvailableProductsByCategorySlug(String categorySlug) {
        Category category = categoryRepo.findBySlug(categorySlug)
                .orElseThrow(NoSuchEntityException::new);

        if (!category.isActive())
            throw new NoSuchDataException();

        List<AbstractProductListResponse> mappedResponse = category.getProducts()
                .stream()
                .filter(BillableProduct::isEnabled)
                .map(p -> productMapperFactory.getMapper(p.getProductType()).mapToList(p))
                .toList();

        if (mappedResponse.isEmpty())
            throw new NoSuchDataException();

        return mappedResponse;
    }
}
