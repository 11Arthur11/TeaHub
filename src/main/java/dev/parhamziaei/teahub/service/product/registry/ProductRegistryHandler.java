package dev.parhamziaei.teahub.service.product.registry;

import dev.parhamziaei.teahub.dto.request.shop.admin.AbstractProductEditRequest;
import dev.parhamziaei.teahub.dto.request.shop.admin.AbstractProductInitRequest;
import dev.parhamziaei.teahub.enums.shop.ProductType;

public interface ProductRegistryHandler {

    ProductType getType();
    void initProduct(AbstractProductInitRequest request);
    void editProduct(Long productId, AbstractProductEditRequest request);

}
