package dev.parhamziaei.teahub.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.integration.teaspeak_query")
public record QueryInstanceProperties(
        Integer portStep

) {
}
