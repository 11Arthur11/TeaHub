package dev.parhamziaei.teahub.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.cookie-factory")
public record CookieFactoryProperties(
        String domain,
        Duration twoFactorCookieTtl,
        Duration phoneVerifyCookieTtl,
        Boolean secureCookie
) {
}
