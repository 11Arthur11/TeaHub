package dev.parhamziaei.teahub.configuration.properties;

import dev.parhamziaei.teahub.integration.teaspeak_query.enums.ProvisionStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.integration.teaspeak-query")
public record QueryInstanceProperties(
        Integer portStep,
        ProvisionStrategy provisionStrategy
) {
}
