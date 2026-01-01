package dev.parhamziaei.teahub.integration.audio_bot.internal_service;

import dev.parhamziaei.teahub.entity.jpa.audio_bot.AudioBotNode;
import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
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

@Component("ROUND_ROBIN")
@RequiredArgsConstructor
public class RoundRobinAudioBotProvisionStrategy implements AudioBotProvisionStrategyHandler {

    private final AtomicInteger pointer = new AtomicInteger(0);
    private final AudioBotNodeRepository audioBotNodeRepository;

    @Override
    public AudioBotNode getProviderNode() {
        List<AudioBotNode> available = audioBotNodeRepository.findAll()
                .stream()
                .filter(ab -> !ab.isFull())
                .sorted(Comparator.comparing(AudioBotNode::getId))
                .toList();

        if (available.isEmpty()) {
            throw new AudioBotProvisionException();
        }

        int index = pointer.getAndIncrement();

        return available.get(Math.floorMod(index, available.size()));
    }

    @Override
    public ProvisionStrategy getType() {
        return ProvisionStrategy.ROUND_ROBIN;
    }
}