package dev.parhamziaei.teahub.configuration.properties;

import dev.parhamziaei.teahub.integration.teaspeak_query.enums.ProvisionStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.integration.audio-bot")
public record AudioBotNodeProperties(
        ProvisionStrategy provisionStrategy
) {

}
