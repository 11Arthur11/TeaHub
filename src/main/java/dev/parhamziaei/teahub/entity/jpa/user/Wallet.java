package dev.parhamziaei.teahub.entity.jpa.user;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
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



}
