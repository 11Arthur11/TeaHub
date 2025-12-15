package dev.parhamziaei.teahub.entity.jpa.payment;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.enums.payment.PaymentGatewayType;
import dev.parhamziaei.teahub.valueobject.Money;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment extends BaseEntity<Long> {

    private String transactionId;
    private String trackingId;

    @Embedded
    private Money amount;

    private PaymentGatewayType gateway;

    @Column(columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime transactionDate;

    @OneToOne(mappedBy = "paymentTransaction", fetch = FetchType.EAGER)
    private Invoice forInvoice;

    @PrePersist
    public void prePersist() {
        transactionDate = LocalDateTime.now().withNano(0);
    }

    public void setForInvoice(Invoice forInvoice) {
        forInvoice.setPaymentTransaction(this);
        this.amount = forInvoice.getMoney();
        this.forInvoice = forInvoice;
    }

}
