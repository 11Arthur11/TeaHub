package dev.parhamziaei.teahub.repository.jpa;

import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.entity.jpa.ticket.Ticket;
import dev.parhamziaei.teahub.entity.jpa.ticket.TicketMessage;
import dev.parhamziaei.teahub.entity.jpa.ticket.TicketMessageAttachment;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.ticket.TicketDepartment;
import dev.parhamziaei.teahub.enums.ticket.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TicketCustomRepository {


    Optional<TicketMessage> addMessage(Long ticketId, TicketMessage ticketMessage);

    Optional<TicketMessageAttachment> findTicketAttachmentByStoredName(String storedName);

    Page<Ticket> findByOwner(Pageable pageable, Long ownerId);



}
