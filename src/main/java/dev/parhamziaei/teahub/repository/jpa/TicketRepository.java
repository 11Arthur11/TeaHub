package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.ticket.Ticket;
import dev.parhamziaei.teahub.entity.jpa.ticket.TicketMessage;
import dev.parhamziaei.teahub.entity.jpa.ticket.TicketMessageAttachment;
import dev.parhamziaei.teahub.enums.ticket.TicketDepartment;
import dev.parhamziaei.teahub.enums.ticket.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TicketRepository {

    void save(Ticket ticket);

    void update(Ticket ticket);

    void delete(Ticket ticket);

    Optional<TicketMessage> addMessage(Long ticketId, TicketMessage ticketMessage);

    Optional<TicketMessageAttachment> findTicketAttachmentByStoredName(String storedName);

    Optional<Ticket> findById(Long id);

    Page<Ticket> findAll(Pageable pageable);

    Page<Ticket> findAllByStatus(TicketStatus status, Pageable pageable);

    Page<Ticket> findAllByDepartment(TicketDepartment department, Pageable pageable);

    Page<Ticket> findAllByStatusAndDepartment(TicketStatus status, TicketDepartment department, Pageable pageable);

    Page<Ticket> findByOwner(Pageable pageable, String ownerPhoneNumber);

}
