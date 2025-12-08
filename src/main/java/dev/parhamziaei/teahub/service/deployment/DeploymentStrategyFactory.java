package dev.parhamziaei.teahub.service.deployment;

import dev.parhamziaei.teahub.configuration.properties.QueryInstanceProperties;
import dev.parhamziaei.teahub.dto.request.resource.AbstractNewResourceRequest;
import dev.parhamziaei.teahub.enums.ResourceType;
import dev.parhamziaei.teahub.service.deployment.strategy.DeploymentStrategyHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DeploymentStrategyFactory {

    private final Map<ResourceType, DeploymentStrategyHandler> strategies;

    public DeploymentStrategyFactory(
            List<DeploymentStrategyHandler> handlers
    ) {
        this.strategies = handlers.stream()
                .collect(Collectors.toMap(DeploymentStrategyHandler::getType, h -> h));
    }

    public DeploymentStrategyHandler getStrategy(ResourceType type) {
        return strategies.get(type);
    }

}
