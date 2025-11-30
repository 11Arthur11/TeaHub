package dev.parhamziaei.teahub.entity.jpa.user;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.enums.InvoiceStatus;
import dev.parhamziaei.teahub.valueobject.Money;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Invoice extends BaseEntity<Long> {

    private String invoiceToken;

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @Embedded
    private Money money;

    @Column(columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime paidAt;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    public Invoice(User owner, Money money) {
        this.owner = owner;
        this.money = money;
        this.invoiceToken = "INVOICE_" + UUID.randomUUID();
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now().withNano(0);
    }

}
