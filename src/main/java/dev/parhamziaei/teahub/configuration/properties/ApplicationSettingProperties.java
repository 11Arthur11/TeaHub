package dev.parhamziaei.teahub.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.settings")
public record ApplicationSettingProperties(
        String backendDomain
) {

}
