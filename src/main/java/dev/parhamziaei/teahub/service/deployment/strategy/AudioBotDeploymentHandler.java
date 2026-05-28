package dev.parhamziaei.teahub.service.deployment.strategy;

import dev.parhamziaei.teahub.dto.request.resource.AbstractNewResourceRequest;
import dev.parhamziaei.teahub.dto.request.resource.user.NewAudioBotResourceRequest;
import dev.parhamziaei.teahub.entity.jpa.resource.AudioBotResource;
import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.entity.jpa.shop.AudioBotProduct;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.audio_bot.AudioBotStatus;
import dev.parhamziaei.teahub.enums.payment.TransactionReason;
import dev.parhamziaei.teahub.enums.shop.ResourceStatus;
import dev.parhamziaei.teahub.enums.shop.ResourceType;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.exception.custom.service.resource.ResourceProvisionException;
import dev.parhamziaei.teahub.kafka.event.resource.AudioBotDeployEvent;
import dev.parhamziaei.teahub.repository.jpa.AudioBotNodeRepository;
import dev.parhamziaei.teahub.repository.jpa.AudioBotProductRepository;
import dev.parhamziaei.teahub.repository.jpa.AudioBotResourceRepository;
import dev.parhamziaei.teahub.repository.jpa.UserRepository;
import dev.parhamziaei.teahub.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AudioBotDeploymentHandler implements DeploymentStrategyHandler {

    private final AudioBotProductRepository audioBotProductRepository;
    private final AudioBotResourceRepository audioBotResourceRepository;
    private final UserRepository userRepository;
    private final WalletService walletService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final AudioBotNodeRepository audioBotNodeRepo;

    @Override
    public ResourceType getType() {
        return ResourceType.AUDIO_BOT;
    }

    @Override
    public <T extends AbstractNewResourceRequest> void initializeDeploy(T request, Long userId) {
        if (!audioBotNodeRepo.isAnyProvisionCandidateAvailable())
            throw new ResourceProvisionException("Cannot deploy " + getType() + " resource, because no Node or Instance found to provide this resource");

        NewAudioBotResourceRequest audioBotRequest = (NewAudioBotResourceRequest) request;

        // ? loading product for resource details
        AudioBotProduct product = audioBotProductRepository.findById(request.getProductId())
                .orElseThrow(NoSuchEntityException::new);

        // ? loading user for giving resource ownership
        User user = userRepository.findById(userId)
                .orElseThrow(NoSuchEntityException::new);

        // ? creating the base resource for deploying
        AudioBotResource resource = AudioBotResource.builder()
                .label(request.getLabel())
                .owner(user)
                .autoProlong(true)
                .orderDate(LocalDateTime.now())
                .expiration(LocalDateTime.now().plus(product.getExpiration()))
                .resourceStatus(ResourceStatus.DEPLOYING)
                .botStatus(AudioBotStatus.OFFLINE)
                .build();

        walletService.debit(
                user.getWallet().getId(),
                product.getPrice().getAmount(),
                TransactionReason.PURCHASE,
                resource.getId()
        );

        // ? adding resource to product resource-list
        product.addUserResource(resource);

        audioBotResourceRepository.save(resource);

        // ? publishing the event
        AudioBotDeployEvent event = new AudioBotDeployEvent(
                product.getId(),
                resource.getId(),
                audioBotRequest
        );

        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void suspend(BillableResource resource) {

    }

    @Override
    public void resume(BillableResource resource) {

    }

    @Override
    public void delete(BillableResource resource) {

    }

}
