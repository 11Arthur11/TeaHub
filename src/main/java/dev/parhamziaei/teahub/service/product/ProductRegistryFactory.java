package dev.parhamziaei.teahub.service.product;

import dev.parhamziaei.teahub.enums.shop.ProductType;
import dev.parhamziaei.teahub.service.product.registry.ProductRegistryHandler;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class ProductRegistryFactory {

    private final Map<ProductType, ProductRegistryHandler> registries;

    public ProductRegistryFactory(List<ProductRegistryHandler> registries) {
        this.registries = registries.stream()
                .collect(Collectors.toMap(ProductRegistryHandler::getType, h -> h));
    }

    public ProductRegistryHandler getHandler(ProductType type) {
        return registries.get(type);
    }

}
