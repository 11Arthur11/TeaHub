package dev.parhamziaei.teahub.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.relational.core.sql.In;

import java.time.Duration;

@ConfigurationProperties("app.components.telnet")
public record TelnetProperties(
        Integer defaultTimeoutMillis,
        Duration reconnectDelay,
        Integer reconnectTries,
        Integer commandTimeout
) {
}
