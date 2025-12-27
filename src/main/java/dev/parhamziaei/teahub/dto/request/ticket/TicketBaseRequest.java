package dev.parhamziaei.teahub.dto.request.ticket;

import dev.parhamziaei.teahub.dto.request.ticket.user.TicketMessageRequest;
import dev.parhamziaei.teahub.enums.ticket.TicketDepartment;

public interface TicketBaseRequest {
    String getSubject();

    TicketDepartment getDepartment();

    Long getRelatedResourceId();

    String getOwnerPhone();

    TicketMessageRequest getMessage();
}
