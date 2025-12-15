package dev.parhamziaei.teahub.entity.jpa.payment;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.entity.jpa.user.Wallet;
import dev.parhamziaei.teahub.enums.payment.TransactionReason;
import dev.parhamziaei.teahub.enums.payment.TransactionType;
import dev.parhamziaei.teahub.valueobject.Money;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransaction extends BaseEntity<Long> {

    private Long relatedResourceId;

    @Enumerated(EnumType.STRING)
    private TransactionReason reason;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(columnDefinition = "TIMESTAMP(0)")
    private LocalDateTime createdAt;

    @Embedded
    private Money amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

}
