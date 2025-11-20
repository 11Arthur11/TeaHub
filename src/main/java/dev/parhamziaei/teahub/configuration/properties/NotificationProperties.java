package dev.parhamziaei.teahub.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.service.notification.properties")
public record NotificationProperties(
        String smsLoginNotifIPPanelPattern
) {
}
