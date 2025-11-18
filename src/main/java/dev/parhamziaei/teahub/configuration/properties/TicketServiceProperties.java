package dev.parhamziaei.teahub.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.service.ticket")
public record TicketServiceProperties(
        Integer maxAttachmentPerMessage
) {
}
