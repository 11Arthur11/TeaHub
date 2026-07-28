package dev.parhamziaei.teahub.repository.jpa.aggregate;

import java.math.BigDecimal;

public record FinanceFlowAggregate(
        Number daily,
        Number weekly,
        Number monthly
) {}
