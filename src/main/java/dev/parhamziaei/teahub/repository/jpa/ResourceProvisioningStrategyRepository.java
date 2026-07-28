package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.ResourceProvisioningStrategy;
import dev.parhamziaei.teahub.enums.shop.ResourceType;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.integration.teaspeak_query.enums.ProvisionStrategy;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceProvisioningStrategyRepository extends JpaRepository<ResourceProvisioningStrategy, ResourceType> {
    default ProvisionStrategy getTeaspeakStrategy() {
        ResourceProvisioningStrategy strategy = findById(ResourceType.TEASPEAK)
                .orElse(new ResourceProvisioningStrategy(ResourceType.TEASPEAK, ProvisionStrategy.BALANCED));

        save(strategy);
        return strategy.getStrategy();
    }

    default ProvisionStrategy getAudioBotStrategy() {
        ResourceProvisioningStrategy strategy = findById(ResourceType.AUDIO_BOT)
                .orElse(new ResourceProvisioningStrategy(ResourceType.AUDIO_BOT, ProvisionStrategy.BALANCED));

        save(strategy);
        return strategy.getStrategy();
    }
}
