package dev.parhamziaei.teahub.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app.service.media-storage")
public record ImageStorageProperties(
        String ticketAttachmentsPath,
        Integer maximumMediaSizeMb,
        List<String> allowedMediaMimeType,
        List<String> allowedMediaExtension
) {}
