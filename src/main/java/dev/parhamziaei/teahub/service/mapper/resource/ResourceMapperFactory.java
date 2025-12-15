package dev.parhamziaei.teahub.service.mapper.resource;

import dev.parhamziaei.teahub.enums.shop.ResourceType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResourceMapperFactory {

    private final Map<ResourceType, ResourceMapperHandler> handlers;

    public ResourceMapperFactory(List<ResourceMapperHandler> handlers) {
        this.handlers = handlers.stream()
                .collect(Collectors.toMap(ResourceMapperHandler::getType,h -> h));
    }

    public ResourceMapperHandler getHandler(ResourceType type) {
        return handlers.get(type);
    }

}
