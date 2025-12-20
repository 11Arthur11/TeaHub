package dev.parhamziaei.teahub.service.interfaces;

import dev.parhamziaei.teahub.dto.request.shop.admin.AbstractProductEditRequest;
import dev.parhamziaei.teahub.dto.request.shop.admin.AbstractProductInitRequest;
import dev.parhamziaei.teahub.dto.response.shop.AbstractProductListResponse;
import dev.parhamziaei.teahub.dto.response.shop.admin.AbstractProductDetailResponse;

import java.util.List;

public interface ProductService {
    AbstractProductDetailResponse getProduct(Long productId);
    List<AbstractProductListResponse> getAllProducts();
    void addProduct(AbstractProductInitRequest initRequest);
    void updateProduct(Long productId, AbstractProductEditRequest editRequest);
    void removeProduct(Long productId);
    void changeEnabled(Long productId, boolean enabled);
    List<? extends AbstractProductListResponse> getAvailableProductsByCategorySlug(String categorySlug);
}
