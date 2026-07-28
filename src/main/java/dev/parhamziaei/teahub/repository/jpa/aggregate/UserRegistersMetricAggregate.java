package dev.parhamziaei.teahub.repository.jpa.aggregate;

public record UserRegistersMetricAggregate(
        Number daily,
        Number weekly,
        Number monthly
) {}