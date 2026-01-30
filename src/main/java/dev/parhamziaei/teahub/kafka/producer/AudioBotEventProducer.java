package dev.parhamziaei.teahub.kafka.producer;

import dev.parhamziaei.teahub.enums.internal.KafkaTopic;
import dev.parhamziaei.teahub.kafka.event.resource.AudioBotDeployEvent;
import dev.parhamziaei.teahub.kafka.event.resource.TeaSpeakDeployEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AudioBotEventProducer {

    private final KafkaTemplate<UUID, Object> kafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    protected void sendDeployEvent(AudioBotDeployEvent event) {
        kafkaTemplate.send(KafkaTopic.AUDIO_BOT_OPERATION_TOPIC.value(), UUID.randomUUID(), event);
    }

}
