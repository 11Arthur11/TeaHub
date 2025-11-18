package dev.parhamziaei.teahub.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "app.initialize")
public record InitializeProperties(
        String adminPhoneNumber,
        String adminEmail,
        String adminFirstName,
        String adminLastName
) {
}
