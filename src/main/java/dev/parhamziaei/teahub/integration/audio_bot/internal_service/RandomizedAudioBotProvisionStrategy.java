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

import java.util.List;
import java.util.Random;

@Component("RANDOMIZED")
@RequiredArgsConstructor
public class RandomizedAudioBotProvisionStrategy implements AudioBotProvisionStrategyHandler {

    private final AudioBotNodeRepository audioBotNodeRepository;
    
    @Override
    public AudioBotNode getProviderNode() {
        List<AudioBotNode> available =  audioBotNodeRepository.findAll()
                .stream()
                .filter(ab -> !ab.isFull())
                .toList();
        if (available.isEmpty())
            throw new AudioBotProvisionException();

        Random random = new Random();
        return available.get(random.nextInt(available.size()));
    }

    @Override
    public ProvisionStrategy getType() {
        return ProvisionStrategy.RANDOMIZED;
    }
}
