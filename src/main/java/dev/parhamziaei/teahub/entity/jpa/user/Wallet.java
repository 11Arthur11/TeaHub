package dev.parhamziaei.teahub.entity.jpa.user;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.valueobject.Money;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "user_wallet")
@Getter
@Setter
public class Wallet extends BaseEntity<Long> {

    @OneToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Embedded
    private Money balance;
}
