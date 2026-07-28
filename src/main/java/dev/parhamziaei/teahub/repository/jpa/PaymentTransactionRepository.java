package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.payment.invoice.PaymentTransaction;
import dev.parhamziaei.teahub.enums.payment.TransactionType;
import dev.parhamziaei.teahub.repository.jpa.aggregate.FinanceFlowAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {

    @Query("""
        SELECT new dev.parhamziaei.teahub.repository.jpa.aggregate.FinanceFlowAggregate(
            COALESCE(SUM(CASE
                WHEN pt.transactionDate >= :dayStart
                 AND pt.transactionDate < :dayEnd
                THEN pt.amount.amount
            END), 0),
            COALESCE(SUM(CASE
                WHEN pt.transactionDate >= :weekStart
                 AND pt.transactionDate < :weekEnd
                THEN pt.amount.amount
            END), 0),
            COALESCE(SUM(CASE
                WHEN pt.transactionDate >= :monthStart
                 AND pt.transactionDate < :monthEnd
                THEN pt.amount.amount
            END), 0)
        )
        FROM PaymentTransaction pt
    """)
    FinanceFlowAggregate aggregate(
            @Param("dayStart") LocalDateTime dayStart,
            @Param("dayEnd") LocalDateTime dayEnd,

            @Param("weekStart") LocalDateTime weekStart,
            @Param("weekEnd") LocalDateTime weekEnd,

            @Param("monthStart") LocalDateTime monthStart,
            @Param("monthEnd") LocalDateTime monthEnd
    );

}
