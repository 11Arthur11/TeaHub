package dev.parhamziaei.teahub.kafka.consumer;

import dev.parhamziaei.teahub.enums.internal.KafkaTopic;
import dev.parhamziaei.teahub.kafka.event.resource.AudioBotDeployEvent;
import dev.parhamziaei.teahub.kafka.handler.AudioBotEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(
        id = "audioBotEventConsumer",
        topics = "audio-bot-operation-topic",
        groupId = "audio-bot-group",
        containerFactory = "kafkaListenerContainerFactory"
)
public class AudioBotEventConsumer {

    private final AudioBotEventHandler audioBotEventHandler;

    @KafkaHandler
    public void consumeDeployEvent(AudioBotDeployEvent event) {
        audioBotEventHandler.handleAudioBotDeploy(event);
    }

}
