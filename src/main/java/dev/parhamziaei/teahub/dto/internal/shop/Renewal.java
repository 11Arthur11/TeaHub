package dev.parhamziaei.teahub.dto.internal.shop;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

public record Renewal(
        LocalDateTime expiration,
        Duration period,
        BigDecimal price
) {}