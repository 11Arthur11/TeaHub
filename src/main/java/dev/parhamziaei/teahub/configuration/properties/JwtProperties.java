package dev.parhamziaei.teahub.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.jason-web-token")
public record JwtProperties(
        Duration twoFactorTokenTtl,
        Duration phoneVerifyTokenTtl,
        Duration refreshTokenTtl,
        Duration accessTokenTtl,
        String base64Secret
) {
}
