package dev.parhamziaei.teahub.kafka.producer;

import dev.parhamziaei.teahub.enums.internal.KafkaTopic;
import dev.parhamziaei.teahub.kafka.event.teaspeak.TelnetSessionLoginFailedEvent;
import dev.parhamziaei.teahub.kafka.event.teaspeak.TelnetSessionReviveFailedEvent;
import dev.parhamziaei.teahub.kafka.event.teaspeak.TelnetSessionUnreachableEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TelnetEventProducer {

    private final KafkaTemplate<UUID, Object> kafkaTemplate;

    public void sendUnreachableEvent(TelnetSessionUnreachableEvent event) {
        kafkaTemplate.send(KafkaTopic.INTERNAL_TELNET_ERROR_TOPIC.value(), UUID.randomUUID(), event);
    }

    public void sendLoginFailedEvent(TelnetSessionLoginFailedEvent event) {
        kafkaTemplate.send(KafkaTopic.INTERNAL_TELNET_ERROR_TOPIC.value(), UUID.randomUUID(), event);
    }

    public void sendReviveFailedEvent(TelnetSessionReviveFailedEvent event) {
        kafkaTemplate.send(KafkaTopic.INTERNAL_TELNET_ERROR_TOPIC.value(), UUID.randomUUID(), event);
    }

}
