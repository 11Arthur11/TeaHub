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

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class RoundRobinAudioBotProvisionStrategy implements AudioBotProvisionStrategyHandler {

    private final AtomicInteger pointer = new AtomicInteger(0);
    private final AudioBotNodeRepository audioBotNodeRepository;
    private final AudioBotGateway audioBotGateway;

    @Override
    public AudioBotNode getProviderNode() {
        List<AudioBotNode> available = audioBotNodeRepository.findProvisionCandidates()
                .stream()
                .sorted(Comparator.comparing(AudioBotNode::getId))
                .toList();

        if (available.isEmpty()) {
            throw new AudioBotProvisionException();
        }

        for (int i = 0; i < available.size(); i++) {
            int index = pointer.getAndIncrement();
            AudioBotNode node = available.get(Math.floorMod(index, available.size()));
            if (audioBotGateway.testConnection(node))
                return node;
        }

        throw new AudioBotProvisionException("Could not find any healthy node with ROUND_ROBIN strategy");
    }

    @Override
    public ProvisionStrategy getType() {
        return ProvisionStrategy.ROUND_ROBIN;
    }
}
