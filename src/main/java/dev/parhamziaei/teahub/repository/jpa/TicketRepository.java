package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.dto.response.dashboard.admin.AdminMetric;
import dev.parhamziaei.teahub.dto.response.dashboard.admin.TicketMetric;
import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.entity.jpa.ticket.Ticket;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.ticket.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.Optional;

public interface TicketRepository extends JpaSpecificationExecutor<Ticket> , JpaRepository<Ticket, Long> , TicketCustomRepository {

    @Query("SELECT t FROM Ticket t WHERE t.id = :ticketId AND t.owner.id = :ownerId")
    Optional<Ticket> findOneByOwnerId(@Param("ownerId") Long userId, @Param("ticketId")Long ticketId);

    default Optional<Ticket> findByOneByPermission(User user, Long ticketId) {
        if (user.isStaff())
            return findById(ticketId);
        else
            return findOneByOwnerId(user.getId(), ticketId);
    }

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.owner.id = :ownerId AND t.status = :status")
    Long countTicketsByStatus(@Param("ownerId") Long userId, @Param("status") TicketStatus ticketStatus);

    @Query("""
        SELECT new dev.parhamziaei.teahub.dto.response.dashboard.admin.TicketMetric(
            COALESCE(SUM(
                        CASE
                            WHEN t.status = 'PENDING'
                            THEN 1
                            ELSE 0
                        END
                    ), 0L),
            COALESCE(SUM(
                        CASE
                            WHEN t.status = 'WAITING'
                            THEN 1
                            ELSE 0
                        END
                    ), 0L),
            COALESCE(SUM(
                        CASE
                            WHEN t.status = 'CLOSED'
                            THEN 1
                            ELSE 0
                        END
                    ), 0L),
            COALESCE(SUM(
                        CASE
                            WHEN t.status = 'RESPONDED'
                            THEN 1
                            ELSE 0
                        END
                    ), 0L)
        ) FROM Ticket t
    """)
    TicketMetric ticketMetric();
}
