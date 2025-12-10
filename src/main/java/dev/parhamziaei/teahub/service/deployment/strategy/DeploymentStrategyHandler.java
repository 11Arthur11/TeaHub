package dev.parhamziaei.teahub.service.deployment.strategy;

import dev.parhamziaei.teahub.dto.request.resource.AbstractNewResourceRequest;
import dev.parhamziaei.teahub.enums.ResourceType;

public interface DeploymentStrategyHandler {

    ResourceType getType();
    <T extends AbstractNewResourceRequest> void produceDeployEvent(T request, Long userId);

}
