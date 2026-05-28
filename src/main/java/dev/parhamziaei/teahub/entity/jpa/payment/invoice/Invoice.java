package dev.parhamziaei.teahub.entity.jpa.payment.invoice;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.payment.InvoiceStatus;
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

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @Embedded
    private Money money;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_id")
    private PaymentTransaction paymentTransaction;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "post_payment_action_id")
    private PostPaymentAction postPaymentAction;

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
        this.status = InvoiceStatus.PENDING;
    }

    public void setPostPaymentAction(PostPaymentAction postPaymentAction) {
        postPaymentAction.setForInvoice(this);
        this.postPaymentAction = postPaymentAction;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now().withNano(0);
    }

}
