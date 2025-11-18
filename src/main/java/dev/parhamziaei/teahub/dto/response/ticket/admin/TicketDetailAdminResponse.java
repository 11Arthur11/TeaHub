package dev.parhamziaei.teahub.dto.response.ticket.admin;

import dev.parhamziaei.teahub.dto.response.ticket.user.TicketDetailBaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TicketDetailAdminResponse extends TicketDetailBaseResponse {

    private String ownerPhone;

    private String ownerFullName;

}
