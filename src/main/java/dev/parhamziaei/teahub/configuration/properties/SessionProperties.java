package dev.parhamziaei.teahub.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.sessions")
public record SessionProperties(
        Duration twoFactorSessionTtl,
        Duration phoneVerifySessionTtl
) {
}
