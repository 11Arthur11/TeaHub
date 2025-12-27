package dev.parhamziaei.teahub.repository.jpa.implement;

import dev.parhamziaei.teahub.entity.jpa.ticket.Ticket;
import dev.parhamziaei.teahub.entity.jpa.ticket.TicketMessage;
import dev.parhamziaei.teahub.entity.jpa.ticket.TicketMessageAttachment;
import dev.parhamziaei.teahub.enums.ticket.TicketDepartment;
import dev.parhamziaei.teahub.enums.ticket.TicketStatus;
import dev.parhamziaei.teahub.repository.jpa.TicketCustomRepository;
import dev.parhamziaei.teahub.repository.jpa.TicketRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TicketRepositoryImpl implements TicketCustomRepository {

    private final EntityManager em;

    @Transactional
    @Override
    public Optional<TicketMessage> addMessage(Long ticketId, TicketMessage ticketMessage) {
        Ticket ticket = em.createQuery("SELECT t FROM Ticket t WHERE t.id =:id", Ticket.class)
                .setParameter("id", ticketId)
                .getSingleResult();

        ticketMessage.setTicket(ticket);
        em.persist(ticketMessage);
        em.flush();
        return Optional.of(ticketMessage);
    }

    @Override
    public Optional<TicketMessageAttachment> findTicketAttachmentByStoredName(String storedName) {
        return em.createQuery("SELECT tma FROM TicketMessageAttachment tma WHERE tma.storedName = :storedName", TicketMessageAttachment.class)
                .setParameter("storedName", storedName)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public Page<Ticket> findByOwner(Pageable pageable, Long ownerId) {
        Query query = em.createQuery("SELECT t FROM Ticket t WHERE t.owner.id = :id", Ticket.class)
                .setParameter("id", ownerId);
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        List<Ticket> tickets = query.getResultList();
        long totalSize = em.createQuery("SELECT COUNT(t) FROM Ticket t WHERE t.owner.id = :id", Long.class)
                .setParameter("id", ownerId)
                .getSingleResult();
        return new PageImpl<>(tickets, pageable, totalSize);
    }
}
