package dev.parhamziaei.teahub.service.implement;

import dev.parhamziaei.teahub.dto.request.shop.admin.AbstractProductEditRequest;
import dev.parhamziaei.teahub.dto.request.shop.admin.AbstractProductInitRequest;
import dev.parhamziaei.teahub.dto.response.shop.admin.AbstractProductListAdminResponse;
import dev.parhamziaei.teahub.dto.response.shop.user.AbstractProductListResponse;
import dev.parhamziaei.teahub.dto.response.shop.admin.AbstractProductDetailResponse;
import dev.parhamziaei.teahub.entity.jpa.shop.BillableProduct;
import dev.parhamziaei.teahub.entity.jpa.shop.Category;
import dev.parhamziaei.teahub.exception.custom.global.EntityInUseException;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchDataException;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.repository.jpa.BillableProductRepository;
import dev.parhamziaei.teahub.repository.jpa.BillableResourceRepository;
import dev.parhamziaei.teahub.repository.jpa.CategoryRepository;
import dev.parhamziaei.teahub.repository.jpa.ProductRepository;
import dev.parhamziaei.teahub.service.interfaces.ProductService;
import dev.parhamziaei.teahub.service.mapper.product.ProductMapperFactory;
import dev.parhamziaei.teahub.service.product.ProductRegistryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final BillableProductRepository billableProductRepo;
    private final CategoryRepository categoryRepo;
    private final ProductRepository productRepository;
    private final ProductMapperFactory productMapperFactory;
    private final ProductRegistryFactory productRegistryFactory;
    private final BillableResourceRepository billableResourceRepo;

    @Override
    public List<AbstractProductListAdminResponse> getAllProducts() {
        List<AbstractProductListAdminResponse> responses = billableProductRepo.findAll()
                .stream()
                .map(p -> {
                    AbstractProductListAdminResponse dto = productMapperFactory.getMapper(p.getProductType()).mapToListForAdmin(p);
                    dto.setCategoryName(p.getCategory().getName());
                    dto.setCategorySlug(p.getCategory().getSlug());
                    dto.setOrderedResources(billableResourceRepo.countByProductId(p.getId()));
                    return dto;
                })
                .toList();

        if (responses.isEmpty())
            throw new NoSuchDataException();
        return responses;
    }

    @Override
    @Transactional
    public AbstractProductDetailResponse getProduct(Long productId) {
        BillableProduct product = billableProductRepo.findById(productId)
                .orElseThrow(NoSuchEntityException::new);
        return productMapperFactory.getMapper(product.getProductType()).mapToDetail(product);
    }

    @Override
    @Transactional
    public void addProduct(AbstractProductInitRequest initRequest) {
        productRegistryFactory.getHandler(initRequest.getType())
                .initProduct(initRequest);
    }

    @Override
    public void updateProduct(Long productId, AbstractProductEditRequest editRequest) {
        productRegistryFactory.getHandler(editRequest.getType())
                .editProduct(productId, editRequest);
    }

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
                .map(p -> {
                    AbstractProductListResponse dto = productMapperFactory.getMapper(p.getProductType()).mapToList(p);
                    dto.setPresentation(p.getPresentation());
                    return dto;
                })
                .sorted(Comparator.comparing(AbstractProductListResponse::getProductType))
                .toList();

        if (mappedResponse.isEmpty())
            throw new NoSuchDataException();

        return mappedResponse;
    }
}
