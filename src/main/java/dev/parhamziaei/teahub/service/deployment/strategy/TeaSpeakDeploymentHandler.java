package dev.parhamziaei.teahub.service.deployment.strategy;

import dev.parhamziaei.teahub.dto.request.resource.AbstractNewResourceRequest;
import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResource;
import dev.parhamziaei.teahub.entity.jpa.shop.TeaSpeakProduct;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.ResourceStatus;
import dev.parhamziaei.teahub.enums.ResourceType;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.kafka.event.resource.TeaSpeakDeployEvent;
import dev.parhamziaei.teahub.kafka.producer.TeaSpeakEventProducer;
import dev.parhamziaei.teahub.repository.jpa.TeaSpeakProductRepository;
import dev.parhamziaei.teahub.repository.jpa.TeaSpeakResourceRepository;
import dev.parhamziaei.teahub.repository.jpa.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("TEASPEAK_DEPLOYER")
@RequiredArgsConstructor
public class TeaSpeakDeploymentHandler implements DeploymentStrategyHandler{

    private final TeaSpeakProductRepository teaSpeakProductRepo;
    private final TeaSpeakEventProducer teaSpeakEventProducer;
    private final TeaSpeakResourceRepository teaSpeakResourceRepository;
    private final UserRepository userRepository;

    @Override
    public ResourceType getType() {
        return ResourceType.TEASPEAK_RESOURCE;
    }

    @Override
    @Transactional
    public <T extends AbstractNewResourceRequest> void produceDeployEvent(T request, Long userId) {
        TeaSpeakProduct product = teaSpeakProductRepo.findById(request.getProductId())
                .orElseThrow(NoSuchEntityException::new);

        User user = userRepository.findById(userId)
                .orElseThrow(NoSuchEntityException::new);

        TeaSpeakResource resource = TeaSpeakResource.builder()
                .label(request.getLabel())
                .owner(user)
                .autoProlong(true)
                .orderDate(LocalDateTime.now())
                .expiration(LocalDateTime.now().plus(product.getExpiration()))
                .status(ResourceStatus.DEPLOYING)
                .build();

        product.addUserResource(resource);

        teaSpeakResourceRepository.save(resource);

        TeaSpeakDeployEvent deployEvent = TeaSpeakDeployEvent.builder()
                .baseResourceId(resource.getId())
                .maxClients(product.getMaxClients())
                .build();

        teaSpeakEventProducer.sendDeployEvent(deployEvent);
    }

}
