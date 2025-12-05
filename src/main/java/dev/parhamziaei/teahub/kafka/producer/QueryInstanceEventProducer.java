package dev.parhamziaei.teahub.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class QueryInstanceEventProducer {

    private final KafkaTemplate<UUID, Object> kafkaTemplate;



}
