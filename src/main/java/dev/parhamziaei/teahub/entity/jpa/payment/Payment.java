package dev.parhamziaei.teahub.entity.jpa.payment;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.enums.GatewayPaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment extends BaseEntity<Long> {

    private String transactionId;
    private String invoiceToken;
    private String trackingId;

    private BigDecimal amount;

    @Column(columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    private GatewayPaymentStatus status;

    @PrePersist
    public void prePersist() {
        transactionDate = LocalDateTime.now().withNano(0);
    }

}
