package dev.parhamziaei.teahub.service.deployment.strategy;

import dev.parhamziaei.teahub.dto.request.resource.AbstractNewResourceRequest;
import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResource;
import dev.parhamziaei.teahub.entity.jpa.resource.TeaSpeakResourceToken;
import dev.parhamziaei.teahub.entity.jpa.shop.TeaSpeakProduct;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.payment.TransactionReason;
import dev.parhamziaei.teahub.enums.shop.ResourceStatus;
import dev.parhamziaei.teahub.enums.shop.ResourceType;
import dev.parhamziaei.teahub.enums.teaspeak.TeaSpeakStatus;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.kafka.event.resource.TeaSpeakDeployEvent;
import dev.parhamziaei.teahub.repository.jpa.TeaSpeakProductRepository;
import dev.parhamziaei.teahub.repository.jpa.TeaSpeakResourceRepository;
import dev.parhamziaei.teahub.repository.jpa.UserRepository;
import dev.parhamziaei.teahub.service.WalletService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component("TEASPEAK_DEPLOYER")
@RequiredArgsConstructor
public class TeaSpeakDeploymentHandler implements DeploymentStrategyHandler{

    private final TeaSpeakProductRepository teaSpeakProductRepo;
    private final TeaSpeakResourceRepository teaSpeakResourceRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final WalletService walletService;

    @Override
    public ResourceType getType() {
        return ResourceType.TEASPEAK_RESOURCE;
    }

    @Override
    @Transactional
    public <T extends AbstractNewResourceRequest> void deploy(T request, Long userId) {
        // ? loading product for resource details
        TeaSpeakProduct product = teaSpeakProductRepo.findById(request.getProductId())
                .orElseThrow(NoSuchEntityException::new);

        // ? loading user for giving resource ownership
        User user = userRepository.findById(userId)
                .orElseThrow(NoSuchEntityException::new);

        // ? creating the base resource for deploying
        TeaSpeakResource resource = TeaSpeakResource.builder()
                .label(request.getLabel())
                .owner(user)
                .autoProlong(true)
                .orderDate(LocalDateTime.now())
                .expiration(LocalDateTime.now().plus(product.getExpiration()))
                .resourceStatus(ResourceStatus.DEPLOYING)
                .teaSpeakStatus(TeaSpeakStatus.OFFLINE)
                .maxClients(product.getMaxClients())
                .build();
        TeaSpeakResourceToken token = new TeaSpeakResourceToken();
        resource.setPrivilegeToken(token);

        walletService.debit(
                user.getWallet().getId(),
                product.getPrice().getAmount(),
                TransactionReason.PURCHASE,
                resource.getId()
        );

        // ? adding resource to product resource-list
        product.addUserResource(resource);

        teaSpeakResourceRepository.save(resource);

        // ? publishing the actual event
        TeaSpeakDeployEvent deployEvent = TeaSpeakDeployEvent.builder()
                .baseResourceId(resource.getId())
                .maxClients(product.getMaxClients())
                .build();

        applicationEventPublisher.publishEvent(deployEvent);
    }

}
