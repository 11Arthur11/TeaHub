package dev.parhamziaei.teahub.service.interfaces;

import dev.parhamziaei.teahub.dto.request.shop.admin.TeaSpeakProductRequest;
import dev.parhamziaei.teahub.dto.response.shop.AbstractProductListResponse;
import dev.parhamziaei.teahub.dto.response.shop.admin.TeaSpeakProductDetailAdminResponse;
import dev.parhamziaei.teahub.dto.response.shop.admin.TeaSpeakProductListAdminResponse;

import java.util.List;

public interface ProductService {
    TeaSpeakProductDetailAdminResponse getTeaSpeakProductById(Long productId);
    List<TeaSpeakProductListAdminResponse> getAllTeaSpeakProducts();
    void initTeaSpeakProduct(TeaSpeakProductRequest initRequest);
    void removeTeaSpeakProduct(Long productId);
    void changeEnabled(Long productId, boolean enabled);
    List<? extends AbstractProductListResponse> getAvailableProductsByCategorySlug(String categorySlug);
}
