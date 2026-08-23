package dev.parhamziaei.teahub.integration;

import dev.parhamziaei.teahub.kafka.configuration.KafkaConsumerConfiguration;
import dev.parhamziaei.teahub.kafka.configuration.KafkaProducerConfiguration;
import dev.parhamziaei.teahub.kafka.event.resource.ResourceExpiredEvent;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.util.ReflectionTestUtils;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers(disabledWithoutDocker = true)
class KafkaEventIT {

    @Container
    static final KafkaContainer KAFKA = new KafkaContainer("apache/kafka:4.1.1");

    @Test
    void resourceEventRoundTripsWithApplicationJsonConfiguration() throws Exception {
        String topic = "resource-event-test-" + UUID.randomUUID();
        createTopic(topic);

        KafkaProducerConfiguration producerConfiguration = new KafkaProducerConfiguration();
        ReflectionTestUtils.setField(producerConfiguration, "bootstrapServers", KAFKA.getBootstrapServers());
        Map<String, Object> producerProperties = producerConfiguration.producerConfig();
        ProducerFactory<UUID, Object> producerFactory =
                producerConfiguration.queryInstanceInitEventProducerFactory(producerProperties);
        KafkaTemplate<UUID, Object> template = producerConfiguration.kafkaTemplate(producerFactory);

        KafkaConsumerConfiguration consumerConfiguration = new KafkaConsumerConfiguration();
        ReflectionTestUtils.setField(consumerConfiguration, "bootstrapServers", KAFKA.getBootstrapServers());
        Map<String, Object> consumerProperties = consumerConfiguration.consumerConfig();
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "resource-event-test-group-" + UUID.randomUUID());
        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        ConsumerFactory<UUID, Object> consumerFactory = consumerConfiguration.consumerFactory(consumerProperties);

        try (Consumer<UUID, Object> consumer = consumerFactory.createConsumer()) {
            consumer.subscribe(java.util.List.of(topic));
            ResourceExpiredEvent event = new ResourceExpiredEvent(42L);
            template.send(topic, UUID.randomUUID(), event).get(10, TimeUnit.SECONDS);

            ConsumerRecord<UUID, Object> record = KafkaTestUtils.getSingleRecord(
                    consumer,
                    topic,
                    Duration.ofSeconds(15)
            );

            assertInstanceOf(ResourceExpiredEvent.class, record.value());
            assertEquals(42L, ((ResourceExpiredEvent) record.value()).getResourceId());
            assertNotNull(record.key());
        } finally {
            template.destroy();
            producerFactory.reset();
        }
    }

    private void createTopic(String topic) throws Exception {
        Map<String, Object> properties = Map.of(
                AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
                KAFKA.getBootstrapServers()
        );
        try (AdminClient admin = AdminClient.create(properties)) {
            admin.createTopics(java.util.List.of(new NewTopic(topic, 1, (short) 1)))
                    .all()
                    .get(10, TimeUnit.SECONDS);
        }
    }
}
