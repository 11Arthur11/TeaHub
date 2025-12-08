package dev.parhamziaei.teahub.service.deployment.strategy;

import dev.parhamziaei.teahub.dto.request.resource.AbstractNewResourceRequest;
import dev.parhamziaei.teahub.dto.request.resource.user.NewTeaSpeakResourceRequest;
import dev.parhamziaei.teahub.entity.jpa.shop.TeaSpeakProduct;
import dev.parhamziaei.teahub.enums.ResourceType;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.kafka.event.resource.TeaSpeakDeployEvent;
import dev.parhamziaei.teahub.kafka.producer.TeaSpeakEventProducer;
import dev.parhamziaei.teahub.repository.jpa.TeaSpeakProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("TEASPEAK_DEPLOYER")
@RequiredArgsConstructor
public class TeaSpeakDeploymentHandler implements DeploymentStrategyHandler{

    private final TeaSpeakProductRepository teaSpeakProductRepo;
    private final TeaSpeakEventProducer teaSpeakEventProducer;

    @Override
    public ResourceType getType() {
        return ResourceType.TEASPEAK;
    }

    @Override
    public <T extends AbstractNewResourceRequest> void produceDeployEvent(T request, Long baseResourceId) {
        TeaSpeakProduct product = teaSpeakProductRepo.findById(request.getProductId())
                .orElseThrow(NoSuchEntityException::new);

        TeaSpeakDeployEvent deployEvent = TeaSpeakDeployEvent.builder()
                .baseResourceId(baseResourceId)
                .maxClients(product.getMaxClients())
                .build();

        teaSpeakEventProducer.sendDeployEvent(deployEvent);
    }

}
