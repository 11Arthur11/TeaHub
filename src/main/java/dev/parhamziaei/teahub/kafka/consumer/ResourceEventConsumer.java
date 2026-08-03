package dev.parhamziaei.teahub.kafka.consumer;

import dev.parhamziaei.teahub.kafka.event.resource.ResourceDeleteEvent;
import dev.parhamziaei.teahub.kafka.event.resource.ResourceDeployFailedEvent;
import dev.parhamziaei.teahub.kafka.event.resource.ResourceExpiredEvent;
import dev.parhamziaei.teahub.kafka.handler.ResourceEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(
        id = "resourceEventConsumer",
        topics = "billable-resource-topic",
        groupId = "resource-group",
        containerFactory = "kafkaListenerContainerFactory"
)
public class ResourceEventConsumer {

    private final ResourceEventHandler resourceEventHandler;

    @KafkaHandler
    protected void handleExpiredEvent(ResourceExpiredEvent event) {
        resourceEventHandler.handleExpiredEvent(event);
    }

    @KafkaHandler
    protected void handleResourceDeleteEvent(ResourceDeleteEvent event) {
        resourceEventHandler.handleResourceDeleteEvent(event);
    }

    @KafkaHandler
    protected void handleDeployFailedEvent(ResourceDeployFailedEvent event) {
        resourceEventHandler.handleDeployFailedEvent(event);
    }

}
