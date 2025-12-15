package dev.parhamziaei.teahub.entity.jpa;

import dev.parhamziaei.teahub.configuration.properties.embedded.InvoiceProperties;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Entity
@Table
@Getter
@Setter
public class ApplicationSetting {

    @Id
    private final Long id = 1L;

    @Embedded
    private InvoiceProperties invoiceProperties = new InvoiceProperties();

}
