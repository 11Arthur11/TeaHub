package dev.parhamziaei.teahub.dto.response.ticket.admin;

import dev.parhamziaei.teahub.dto.response.ticket.user.TicketDetailBaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(
        description = "'ownerId' field requires UserDetail Page redirect"
)
public class TicketDetailAdminResponse extends TicketDetailBaseResponse {

    private Long ownerId;

    private String ownerFullName;

}
