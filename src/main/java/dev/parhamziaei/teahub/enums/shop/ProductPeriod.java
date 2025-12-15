package dev.parhamziaei.teahub.enums.shop;

import java.time.Duration;

public enum ProductPeriod {
    HOURLY(Duration.ofHours(1), "product-period.hourly"),
    DAILY(Duration.ofDays(1), "product-period.daily"),
    MONTHLY(Duration.ofDays(30), "product-period.monthly"),
    BIMONTHLY(Duration.ofDays(60), "product-period.bimonthly"),
    QUARTERLY(Duration.ofDays(90), "product-period.quarterly"),
    SEMIANNUAL(Duration.ofDays(182), "product-period.semiannual"),
    ANNUAL(Duration.ofDays(365), "product-period.annual"),;

    private final Duration duration;
    private final String key;

    ProductPeriod(Duration duration, String key) {
        this.duration = duration;
        this.key = key;
    }
    public Duration duration() {
        return this.duration;
    }

    public String key() {
        return this.key;
    }
}
