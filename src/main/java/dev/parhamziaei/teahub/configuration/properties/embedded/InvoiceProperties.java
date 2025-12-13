package dev.parhamziaei.teahub.configuration.properties.embedded;

import jakarta.persistence.Embeddable;

import java.time.Duration;
import java.time.LocalDateTime;

@Embeddable
public class InvoiceProperties {

    private Duration monthlyResourceInvoiceOffset = Duration.ofDays(7);
    private Duration dailyResourceInvoiceOffset = Duration.ofHours(4);
    private Duration dueDate = Duration.ofDays(7);

}
