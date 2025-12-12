package dev.parhamziaei.teahub.kafka.publisher;

import dev.parhamziaei.teahub.kafka.event.resource.TeaSpeakDeployEvent;
import dev.parhamziaei.teahub.kafka.producer.TeaSpeakEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TeaSpeakEventPublisher {

    private final TeaSpeakEventProducer producer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDeployEvent(TeaSpeakDeployEvent event) {
        producer.sendDeployEvent(event);
    }

}
