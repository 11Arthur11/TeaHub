package dev.parhamziaei.teahub.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.integration.ip-panel")
public record IPPanelProperties(
        String baseUrl,
        String apiKey,
        String twoFactoMessagePatternCode,
        String fromNumber
) {
}
