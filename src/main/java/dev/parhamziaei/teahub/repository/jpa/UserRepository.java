package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.payment.TransactionType;
import dev.parhamziaei.teahub.repository.jpa.aggregate.FinanceFlowAggregate;
import dev.parhamziaei.teahub.repository.jpa.aggregate.UserRegistersMetricAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface UserRepository extends JpaSpecificationExecutor<User>, JpaRepository<User, Long>, UserCustomRepository {

    @Query("""
        SELECT new dev.parhamziaei.teahub.repository.jpa.aggregate.UserRegistersMetricAggregate(
            COALESCE(COUNT(CASE
                WHEN u.createdAt >= :dayStart
                 AND u.createdAt < :dayEnd
                THEN 1
            END), 0L),
    
            COALESCE(COUNT(CASE
                WHEN u.createdAt >= :weekStart
                 AND u.createdAt < :weekEnd
                THEN 1
            END), 0L),
    
            COALESCE(COUNT(CASE
                WHEN u.createdAt >= :monthStart
                 AND u.createdAt < :monthEnd
                THEN 1
            END), 0L)
        )
        FROM User u
    """)
    UserRegistersMetricAggregate aggregateUserRegisters(
            @Param("dayStart") LocalDateTime dayStart,
            @Param("dayEnd") LocalDateTime dayEnd,

            @Param("weekStart") LocalDateTime weekStart,
            @Param("weekEnd") LocalDateTime weekEnd,

            @Param("monthStart") LocalDateTime monthStart,
            @Param("monthEnd") LocalDateTime monthEnd
    );


}
