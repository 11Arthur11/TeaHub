package dev.parhamziaei.teahub.kafka.consumer;

import dev.parhamziaei.teahub.kafka.event.resource.TeaSpeakDeployEvent;
import dev.parhamziaei.teahub.kafka.handler.TeaSpeakEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(
        id = "teaSpeakEventConsumer",
        topics = "teaspeak-operation-topic",
        groupId = "teaspeak-group",
        containerFactory = "kafkaListenerContainerFactory"
)
public class TeaSpeakEventConsumer {

    private final TeaSpeakEventHandler teaSpeakEventHandler;

    @KafkaHandler
    public void handleDeployEvent(TeaSpeakDeployEvent event) {
        teaSpeakEventHandler.handeTeaSpeakDeployEvent(event);
    }

}
