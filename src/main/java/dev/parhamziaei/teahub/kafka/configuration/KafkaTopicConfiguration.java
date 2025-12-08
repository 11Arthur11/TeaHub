package dev.parhamziaei.teahub.kafka.configuration;

import dev.parhamziaei.teahub.enums.KafkaTopic;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfiguration {

    @Bean
    public NewTopic telnetErrorTopic() {
        return TopicBuilder.name(KafkaTopic.INTERNAL_TELNET_ERROR_TOPIC.value()).build();
    }

    @Bean
    public NewTopic teaSpeakTopic() {
        return TopicBuilder.name(KafkaTopic.TEASPEAK_OPERATION_TOPIC.value()).build();
    }

    @Bean
    public NewTopic queryInstanceTopic() {
        return TopicBuilder.name(KafkaTopic.INTERNAL_QUERY_INSTANCE_TOPIC.value()).build();
    }

}
