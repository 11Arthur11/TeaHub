package dev.parhamziaei.teahub.service.deployment.strategy;

import dev.parhamziaei.teahub.dto.request.resource.AbstractNewResourceRequest;
import dev.parhamziaei.teahub.enums.shop.ResourceType;

public interface DeploymentStrategyHandler {

    ResourceType getType();
    <T extends AbstractNewResourceRequest> void deploy(T request, Long userId);

}
