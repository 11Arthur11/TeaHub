package dev.parhamziaei.teahub.dto.request.ticket.admin;

import dev.parhamziaei.teahub.dto.request.ticket.AbstractTicketRequest;
import dev.parhamziaei.teahub.validation.annotation.PhoneNumber;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TicketAdminRequest extends AbstractTicketRequest {

    private Long targetUserId;

}
