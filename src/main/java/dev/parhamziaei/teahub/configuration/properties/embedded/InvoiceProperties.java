package dev.parhamziaei.teahub.configuration.properties.embedded;

import jakarta.persistence.Embeddable;

import java.time.Duration;
import java.time.LocalDateTime;

@Embeddable
public class InvoiceProperties {

    private Duration invoiceCreationBeforeServiceExpire = Duration.ofDays(7);
    private Duration dueDate = Duration.ofDays(7);

}
