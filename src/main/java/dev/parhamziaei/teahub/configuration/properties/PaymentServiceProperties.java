package dev.parhamziaei.teahub.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@ConfigurationProperties("app.service.payment")
public record PaymentServiceProperties(
        BigDecimal minimumWalletChargeAmountIrt,
        Integer taxPercentage
) {
}
