package dev.parhamziaei.teahub.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.service.payment")
public record PaymentServiceProperties(
        Integer taxPercentage,
        String aghayePardakhtPinCode
) {
}
