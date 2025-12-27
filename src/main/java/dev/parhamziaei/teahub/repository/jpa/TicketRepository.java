package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.entity.jpa.ticket.Ticket;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

}
