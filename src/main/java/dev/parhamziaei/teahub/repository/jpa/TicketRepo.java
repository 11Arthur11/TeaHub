package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.Ticket;
import dev.parhamziaei.teahub.entity.jpa.TicketMessage;
import dev.parhamziaei.teahub.entity.jpa.TicketMessageAttachment;
import dev.parhamziaei.teahub.enums.TicketDepartment;
import dev.parhamziaei.teahub.enums.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TicketRepo {

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
