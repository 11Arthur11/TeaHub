package dev.parhamziaei.teahub.entity.jpa;

import dev.parhamziaei.teahub.valueobject.InvoiceProperties;
import dev.parhamziaei.teahub.valueobject.ResourceProperties;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class ApplicationSettings {

    @Id
    private final Long id = 1L;

    @Embedded
    private InvoiceProperties invoiceProperties = new InvoiceProperties();

    @Embedded
    private ResourceProperties resourceProperties = new ResourceProperties();

}
