package dev.parhamziaei.teahub.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.components.telnet")
public record TelnetProperties(
        Integer defaultTimeoutMillis
) {
}
