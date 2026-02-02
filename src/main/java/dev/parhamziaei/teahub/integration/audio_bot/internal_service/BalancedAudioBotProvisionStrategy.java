package dev.parhamziaei.teahub.integration.audio_bot.internal_service;

import dev.parhamziaei.teahub.entity.jpa.audio_bot.AudioBotNode;
import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.integration.audio_bot.component.AudioBotGateway;
import dev.parhamziaei.teahub.integration.audio_bot.exception.AudioBotProvisionException;
import dev.parhamziaei.teahub.integration.teaspeak_query.enums.ProvisionStrategy;
import dev.parhamziaei.teahub.integration.teaspeak_query.exception.QueryProvisionException;
import dev.parhamziaei.teahub.repository.jpa.AudioBotNodeRepository;
import dev.parhamziaei.teahub.repository.jpa.QueryInstanceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BalancedAudioBotProvisionStrategy implements AudioBotProvisionStrategyHandler {

    private final AudioBotNodeRepository audioBotNodeRepository;
    private final AudioBotGateway audioBotGateway;

    @Override
    public AudioBotNode getProviderNode() {
        List<AudioBotNode> candidates = audioBotNodeRepository.findProvisionCandidates()
                .stream()
                .sorted(Comparator.comparing(ab -> ab.getInstances().size()))
                .toList();

        for (AudioBotNode candidate : candidates) {
            if (audioBotGateway.testConnection(candidate)) {
                return candidate;
            }
        }
        throw new AudioBotProvisionException("Could not find any healthy node with BALANCED strategy");
    }

    @Override
    public ProvisionStrategy getType() {
        return ProvisionStrategy.BALANCED;
    }

}
