package dev.parhamziaei.teahub.integration.audio_bot.internal_service;

import dev.parhamziaei.teahub.entity.jpa.ResourceProvisioningStrategy;
import dev.parhamziaei.teahub.integration.teaspeak_query.enums.ProvisionStrategy;
import dev.parhamziaei.teahub.repository.jpa.ResourceProvisioningStrategyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AudioBotProvisionStrategyFactory {

    private final Map<ProvisionStrategy, AudioBotProvisionStrategyHandler> strategies;
    private final ResourceProvisioningStrategyRepository provisionStrategyRepo;

    public AudioBotProvisionStrategyFactory(
            List<AudioBotProvisionStrategyHandler> handlers,
            ResourceProvisioningStrategyRepository provisionStrategyRepo
    ) {
        this.strategies = handlers.stream()
                .collect(Collectors.toMap(AudioBotProvisionStrategyHandler::getType, h -> h));
        this.provisionStrategyRepo = provisionStrategyRepo;
    }

    public AudioBotProvisionStrategyHandler getStrategy() {
        return strategies.get(provisionStrategyRepo.getAudioBotStrategy());
    }

}
