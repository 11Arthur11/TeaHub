package dev.parhamziaei.teahub.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.jason-web-token")
public record JwtProperties(
        Duration twoFactorTokenTtl,
        Duration refreshTokenTtl,
        Duration accessTokenTtl,
        Duration forgotPasswordTokenTtl,
        String base64Secret
) {
}
