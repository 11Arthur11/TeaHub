package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.ticket.Ticket;
import dev.parhamziaei.teahub.entity.jpa.ticket.TicketMessage;
import dev.parhamziaei.teahub.entity.jpa.ticket.TicketMessageAttachment;
import dev.parhamziaei.teahub.enums.TicketDepartment;
import dev.parhamziaei.teahub.enums.TicketStatus;
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
public class TicketRepositoryImpl implements TicketRepository {

    private final EntityManager em;

    @Transactional
    @Override
    public void save(Ticket ticket) {
        em.persist(ticket);
    }

    @Transactional
    @Override
    public void update(Ticket ticket) {
        em.merge(ticket);
    }

    @Transactional
    @Override
    public void delete(Ticket ticket) {
        em.remove(ticket);
    }

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
    public Optional<Ticket> findById(Long id) {
        return em.createQuery("SELECT t FROM Ticket t WHERE t.id = :id", Ticket.class).setParameter("id", id)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public Page<Ticket> findAll(Pageable pageable) {
        Query query = em.createQuery("FROM Ticket", Ticket.class);
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        List<Ticket> tickets = query.getResultList();
        long totalSize = em.createQuery("SELECT COUNT(t) FROM Ticket t", Long.class).getSingleResult();
        return new PageImpl<>(tickets, pageable, totalSize);
    }

    @Override
    public Page<Ticket> findAllByStatus(TicketStatus status, Pageable pageable) {
        Query query = em.createQuery("SELECT t FROM Ticket t WHERE t.status =:status", Ticket.class);
        query.setParameter("status", status.value());
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        List<Ticket> tickets = query.getResultList();
        long totalSize = em.createQuery("SELECT COUNT(t) FROM Ticket t WHERE t.status =:status", Long.class)
                .setParameter("status", status.value())
                .getSingleResult();
        return new PageImpl<>(tickets, pageable, totalSize);
    }

    @Override
    public Page<Ticket> findAllByDepartment(TicketDepartment department, Pageable pageable) {
        Query query = em.createQuery("SELECT t FROM Ticket t WHERE t.department =:department", Ticket.class);
        query.setParameter("department", department.value());
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        List<Ticket> tickets = query.getResultList();
        long totalSize = em.createQuery("SELECT COUNT(t) FROM Ticket t WHERE t.department =:department", Long.class)
                .setParameter("department", department.value())
                .getSingleResult();
        return new PageImpl<>(tickets, pageable, totalSize);
    }

    @Override
    public Page<Ticket> findAllByStatusAndDepartment(TicketStatus status, TicketDepartment department, Pageable pageable) {
        Query query = em.createQuery("SELECT t FROM Ticket t WHERE t.department =:department AND t.status =:status", Ticket.class);
        query.setParameter("department", department.value());
        query.setParameter("status", status.value());
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        List<Ticket> tickets = query.getResultList();
        long totalSize = em.createQuery("SELECT COUNT(t) FROM Ticket t WHERE t.department =:department AND t.status =:status", Long.class)
                .setParameter("department", department.value())
                .setParameter("status", status.value())
                .getSingleResult();
        return new PageImpl<>(tickets, pageable, totalSize);
    }

    @Override
    public Page<Ticket> findByOwner(Pageable pageable, String ownerPhoneNumber) {
        Query query = em.createQuery("SELECT t FROM Ticket t WHERE t.ownerPhone = :ownerPhone", Ticket.class)
                .setParameter("ownerPhone", ownerPhoneNumber);
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        List<Ticket> tickets = query.getResultList();
        long totalSize = em.createQuery("SELECT COUNT(t) FROM Ticket t WHERE t.ownerPhone = :ownerPhone", Long.class)
                .setParameter("ownerPhone", ownerPhoneNumber)
                .getSingleResult();
        return new PageImpl<>(tickets, pageable, totalSize);
    }
}
