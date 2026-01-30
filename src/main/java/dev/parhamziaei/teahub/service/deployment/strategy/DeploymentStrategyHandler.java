package dev.parhamziaei.teahub.service.deployment.strategy;

import dev.parhamziaei.teahub.dto.request.resource.AbstractNewResourceRequest;
import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.enums.shop.ResourceType;

public interface DeploymentStrategyHandler {

    ResourceType getType();
    <T extends AbstractNewResourceRequest> void initializeDeploy(T request, Long userId);
    void suspend(BillableResource resource);
    void resume(BillableResource resource);
    void delete(BillableResource resource);

}
