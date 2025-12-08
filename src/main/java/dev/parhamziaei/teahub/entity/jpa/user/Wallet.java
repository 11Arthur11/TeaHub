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

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User owner;

    @Embedded
    private Money balance;

    public Wallet() {
        this.balance = new Money(BigDecimal.ZERO);
    }
}
