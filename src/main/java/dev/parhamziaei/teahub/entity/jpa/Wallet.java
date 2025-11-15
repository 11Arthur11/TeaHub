package dev.parhamziaei.teahub.entity.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "user_wallets")
@Getter
@Setter
public class Wallet extends BaseEntity<Long> {

    @OneToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(precision = 19, scale = 2, name = "balance")
    private BigDecimal balance = BigDecimal.ZERO;

}
