package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.payment.WalletTransaction;
import dev.parhamziaei.teahub.enums.payment.TransactionType;
import dev.parhamziaei.teahub.repository.jpa.aggregate.FinanceFlowAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface WalletTransactionRepository extends JpaSpecificationExecutor<WalletTransaction>, JpaRepository<WalletTransaction, Long> {

    @Query("""
        SELECT new dev.parhamziaei.teahub.repository.jpa.aggregate.FinanceFlowAggregate(
            COALESCE(SUM(CASE
                WHEN wt.createdAt >= :dayStart
                 AND wt.createdAt < :dayEnd
                THEN wt.amount.amount
            END), 0),
            COALESCE(SUM(CASE
                WHEN wt.createdAt >= :weekStart
                 AND wt.createdAt < :weekEnd
                THEN wt.amount.amount
            END), 0),
            COALESCE(SUM(CASE
                WHEN wt.createdAt >= :monthStart
                 AND wt.createdAt < :monthEnd
                THEN wt.amount.amount
            END), 0)
        )
        FROM WalletTransaction wt
        WHERE wt.type = :type
    """)
    FinanceFlowAggregate aggregate(
            @Param("type") TransactionType type,

            @Param("dayStart") LocalDateTime dayStart,
            @Param("dayEnd") LocalDateTime dayEnd,

            @Param("weekStart") LocalDateTime weekStart,
            @Param("weekEnd") LocalDateTime weekEnd,

            @Param("monthStart") LocalDateTime monthStart,
            @Param("monthEnd") LocalDateTime monthEnd
    );

}
