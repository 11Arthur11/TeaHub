package dev.parhamziaei.teahub.kafka.producer;

import dev.parhamziaei.teahub.enums.internal.KafkaTopic;
import dev.parhamziaei.teahub.kafka.event.resource.ResourceDeleteEvent;
import dev.parhamziaei.teahub.kafka.event.resource.ResourceDeployFailedEvent;
import dev.parhamziaei.teahub.kafka.event.resource.ResourceExpiredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ResourceEventProducer {

    private final KafkaTemplate<UUID, Object> kafkaTemplate;

    public void sendResourceExpiredEvent(Long resourceId) {
        ResourceExpiredEvent event = new ResourceExpiredEvent(resourceId);
        kafkaTemplate.send(KafkaTopic.BILLABLE_RESOURCE_TOPIC.value(), UUID.randomUUID(), event);
    }

    public void sendResourceDeleteEvent(Long resourceId) {
        ResourceDeleteEvent event = new ResourceDeleteEvent(resourceId);
        kafkaTemplate.send(KafkaTopic.BILLABLE_RESOURCE_TOPIC.value(), UUID.randomUUID(), event);
    }

    public void sendDeployFailedEvent(Long resourceId) {
        ResourceDeployFailedEvent event = new ResourceDeployFailedEvent(resourceId);
        kafkaTemplate.send(KafkaTopic.BILLABLE_RESOURCE_TOPIC.value(), UUID.randomUUID(), event);
    }

}
