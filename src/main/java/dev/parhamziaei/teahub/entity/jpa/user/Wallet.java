package dev.parhamziaei.teahub.entity.jpa.user;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import dev.parhamziaei.teahub.entity.jpa.payment.WalletTransaction;
import dev.parhamziaei.teahub.valueobject.Money;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_wallet")
@Getter
@Setter
public class Wallet extends BaseEntity<Long> {

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User owner;

    @Embedded
    private Money balance;

    @OneToMany(mappedBy = "wallet", fetch = FetchType.LAZY)
    private List<WalletTransaction> transactions;

    public Wallet() {
        this.balance = new Money(BigDecimal.ZERO);
    }

    public void addTransaction(WalletTransaction walletTransaction) {
        if (this.transactions == null)
            this.transactions = new ArrayList<>();
        walletTransaction.setWallet(this);
        this.transactions.add(walletTransaction);
    }
}
