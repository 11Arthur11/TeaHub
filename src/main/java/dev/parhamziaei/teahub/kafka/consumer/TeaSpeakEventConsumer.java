package dev.parhamziaei.teahub.kafka.consumer;

import dev.parhamziaei.teahub.kafka.event.resource.TeaSpeakDeployEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;

@RequiredArgsConstructor
@KafkaListener(
        id = "teaSpeakEventConsumer",
        topics = "teaspeak-operation-topic",
        groupId = "teaspeak-group",
        containerFactory = "kafkaListenerContainerFactory"
)
public class TeaSpeakEventConsumer {

    @KafkaHandler
    public void handleDeployEvent(TeaSpeakDeployEvent event) {

    }

}
