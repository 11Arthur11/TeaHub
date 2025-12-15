package dev.parhamziaei.teahub.configuration.properties.embedded;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
public class InvoiceProperties {

    private Duration monthlyResourcePreBilling = Duration.ofDays(7);
    private Duration hourlyResourcePreBilling = Duration.ofHours(4);
    private Duration dueDate = Duration.ofDays(7);

}
