package dev.parhamziaei.teahub.service.interfaces;

import dev.parhamziaei.teahub.dto.request.teaspeak.admin.TeaSpeakProductInitRequest;
import dev.parhamziaei.teahub.dto.request.teaspeak.admin.TeaSpeakProductUpdateRequest;
import dev.parhamziaei.teahub.dto.response.shop.admin.TeaSpeakProductDetailAdminResponse;
import dev.parhamziaei.teahub.dto.response.shop.admin.TeaSpeakProductListAdminResponse;

import java.util.List;

public interface ProductService {
    TeaSpeakProductDetailAdminResponse getTeaSpeakProductById(Long productId);
    List<TeaSpeakProductListAdminResponse> getAllTeaSpeakProducts();
    void initTeaSpeakProduct(TeaSpeakProductInitRequest initRequest);
    void updateTeaSpeakProduct(TeaSpeakProductUpdateRequest updateRequest);
    void removeTeaSpeakProduct(Long productId);

}
