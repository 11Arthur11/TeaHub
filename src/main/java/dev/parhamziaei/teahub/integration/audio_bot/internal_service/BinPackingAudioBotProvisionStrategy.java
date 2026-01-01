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

@Component("BIN_PACKING")
@RequiredArgsConstructor
public class BinPackingAudioBotProvisionStrategy implements AudioBotProvisionStrategyHandler {

    private final AudioBotNodeRepository audioBotNodeRepository;

    @Override
    public AudioBotNode getProviderNode() {
        return audioBotNodeRepository.findAll()
                .stream()
                .filter(ab -> !ab.isFull())
                .min(Comparator.comparing(AudioBotNode::getId))
                .orElseThrow(AudioBotProvisionException::new);
    }

    @Override
    public ProvisionStrategy getType() {
        return ProvisionStrategy.BIN_PACKING;
    }
}
