package dev.parhamziaei.teahub.integration.audio_bot.internal_service;

import dev.parhamziaei.teahub.entity.jpa.audio_bot.AudioBotNode;
import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.integration.audio_bot.component.AudioBotGateway;
import dev.parhamziaei.teahub.integration.audio_bot.exception.AudioBotProvisionException;
import dev.parhamziaei.teahub.integration.teaspeak_query.enums.ProvisionStrategy;
import dev.parhamziaei.teahub.integration.teaspeak_query.exception.QueryProvisionException;
import dev.parhamziaei.teahub.repository.jpa.AudioBotNodeRepository;
import dev.parhamziaei.teahub.repository.jpa.QueryInstanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component("RANDOMIZED")
@RequiredArgsConstructor
public class RandomizedAudioBotProvisionStrategy implements AudioBotProvisionStrategyHandler {

    private final AudioBotNodeRepository audioBotNodeRepository;
    private final AudioBotGateway audioBotGateway;

    @Override
    public AudioBotNode getProviderNode() {
        List<AudioBotNode> candidates =  audioBotNodeRepository.findProvisionCandidates();

        if (candidates.isEmpty())
            throw new AudioBotProvisionException("No available audio bot nodes found");

        Random random = new Random();
        for (int index = 0; index < candidates.size(); index++) {
            AudioBotNode node = candidates.get(random.nextInt(candidates.size()));
            if (audioBotGateway.testConnection(node))
                return node;
            else
                candidates.remove(node);
        }

        throw new AudioBotProvisionException("Could not find any healthy node with RANDOMIZED strategy");
    }

    @Override
    public ProvisionStrategy getType() {
        return ProvisionStrategy.RANDOMIZED;
    }

}
