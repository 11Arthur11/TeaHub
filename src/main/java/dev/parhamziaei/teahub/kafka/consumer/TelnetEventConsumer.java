package dev.parhamziaei.teahub.kafka.consumer;

import dev.parhamziaei.teahub.kafka.event.teaspeak.TelnetSessionLoginFailedEvent;
import dev.parhamziaei.teahub.kafka.event.teaspeak.TelnetSessionReviveFailedEvent;
import dev.parhamziaei.teahub.kafka.event.teaspeak.TelnetSessionUnreachableEvent;
import dev.parhamziaei.teahub.kafka.handler.TelnetEventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@KafkaListener(
        id = "telnetEventConsumer",
        topics = "internal-telnet-topic",
        groupId = "internal-telnet-events",
        containerFactory = "kafkaListenerContainerFactory"
)
public class TelnetEventConsumer {

    private final TelnetEventHandler telnetEventHandler;

    @KafkaHandler
    public void consumeUnreachableEvent(TelnetSessionUnreachableEvent event) {
        telnetEventHandler.handleUnreachableEvent(event);
    }

    @KafkaHandler
    public void consumeLoginFailedEvent(TelnetSessionLoginFailedEvent event) {

    }

    @KafkaHandler
    public void consumeReviveFailedEvent(TelnetSessionReviveFailedEvent event) {

    }

}
