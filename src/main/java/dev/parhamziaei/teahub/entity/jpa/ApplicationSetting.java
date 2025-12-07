package dev.parhamziaei.teahub.entity.jpa;

import dev.parhamziaei.teahub.configuration.properties.embedded.InvoiceProperties;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Duration;

@Entity
@Table
public class ApplicationSetting {

    @Id
    private final Long id = 1L;

    @Embedded
    private InvoiceProperties invoiceProperties = new InvoiceProperties();

}
