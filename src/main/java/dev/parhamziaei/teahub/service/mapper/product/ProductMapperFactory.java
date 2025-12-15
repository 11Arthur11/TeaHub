package dev.parhamziaei.teahub.service.mapper.product;

import dev.parhamziaei.teahub.enums.shop.ProductType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductMapperFactory {

    private final Map<ProductType, ProductMapperHandler> handlers;

    public ProductMapperFactory(List<ProductMapperHandler> handlers) {
        this.handlers = handlers.stream()
                .collect(Collectors.toMap(ProductMapperHandler::getType , h -> h));
    }

    public ProductMapperHandler getMapper(ProductType type) {
        return handlers.get(type);
    }

}
