package dev.parhamziaei.teahub.service.interfaces;

import dev.parhamziaei.teahub.dto.request.shop.admin.TeaSpeakProductInitRequest;
import dev.parhamziaei.teahub.dto.request.shop.admin.TeaSpeakProductUpdateRequest;
import dev.parhamziaei.teahub.dto.response.shop.TeaSpeakProductDTO;
import dev.parhamziaei.teahub.dto.response.shop.admin.TeaSpeakProductDetailAdminResponse;
import dev.parhamziaei.teahub.dto.response.shop.admin.TeaSpeakProductListAdminResponse;

import java.util.List;

public interface ProductService {
    TeaSpeakProductDetailAdminResponse getTeaSpeakProductById(Long productId);
    List<TeaSpeakProductListAdminResponse> getAllTeaSpeakProducts();
    void initTeaSpeakProduct(TeaSpeakProductInitRequest initRequest);
    void updateTeaSpeakProduct(TeaSpeakProductUpdateRequest updateRequest);
    void removeTeaSpeakProduct(Long productId);
    void changeEnabled(Long productId, boolean enabled);
    void updateCategoryProductsType(Long oldCategoryId);
    List<? extends TeaSpeakProductDTO> getAvailableProductsByCategorySlug(String categorySlug);
}
