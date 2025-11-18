package dev.parhamziaei.teahub.dto.request.ticket;

import dev.parhamziaei.teahub.dto.request.ticket.user.TicketMessageRequest;

public interface TicketBaseRequest {
    String getSubject();

    String getDepartment();

    String getServiceName();

    String getOwnerPhone();

    TicketMessageRequest getMessage();
}
