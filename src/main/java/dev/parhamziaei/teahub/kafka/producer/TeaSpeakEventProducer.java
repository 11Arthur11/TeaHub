package dev.parhamziaei.teahub.kafka.producer;

import dev.parhamziaei.teahub.enums.KafkaTopic;
import dev.parhamziaei.teahub.kafka.event.resource.TeaSpeakDeployEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TeaSpeakEventProducer {

    private final KafkaTemplate<UUID, Object> kafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendDeployEvent(TeaSpeakDeployEvent event) {
        kafkaTemplate.send(KafkaTopic.TEASPEAK_OPERATION_TOPIC.value(), UUID.randomUUID(), event);
    }
}
