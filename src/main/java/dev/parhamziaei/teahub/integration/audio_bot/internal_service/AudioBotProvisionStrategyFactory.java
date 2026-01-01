package dev.parhamziaei.teahub.integration.audio_bot.internal_service;

import dev.parhamziaei.teahub.configuration.properties.AudioBotNodeProperties;
import dev.parhamziaei.teahub.configuration.properties.QueryInstanceProperties;
import dev.parhamziaei.teahub.integration.teaspeak_query.enums.ProvisionStrategy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AudioBotProvisionStrategyFactory {

    private final Map<ProvisionStrategy, AudioBotProvisionStrategyHandler> strategies;
    private final AudioBotNodeProperties audioBotNodeProperties;

    public AudioBotProvisionStrategyFactory(
            List<AudioBotProvisionStrategyHandler> handlers,
            AudioBotNodeProperties audioBotNodeProperties
    ) {
        this.strategies = handlers.stream()
                .collect(Collectors.toMap(AudioBotProvisionStrategyHandler::getType, h -> h));
        this.audioBotNodeProperties = audioBotNodeProperties;
    }

    public AudioBotProvisionStrategyHandler getStrategy() {
        return strategies.get(audioBotNodeProperties.provisionStrategy());
    }

}
