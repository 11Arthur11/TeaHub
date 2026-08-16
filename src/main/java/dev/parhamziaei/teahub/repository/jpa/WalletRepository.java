package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.user.Wallet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface WalletRepository extends JpaSpecificationExecutor<Wallet>, JpaRepository<Wallet, Long> {

    @Query("SELECT w FROM Wallet w WHERE w.id = :id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Wallet findByIdAndLock(Long id);

    @Query("SELECT w FROM Wallet w WHERE w.owner.id = :id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Wallet findByOwnerIdAndLock(Long id);


    @Query("""
    SELECT COALESCE(SUM(w.balance.amount), 0)
    FROM Wallet w
    """)
    BigDecimal sumAllWalletBalances();

}
