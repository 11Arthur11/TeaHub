package dev.parhamziaei.teahub.dto.response.ticket;

import dev.parhamziaei.teahub.enums.ticket.TicketDepartment;
import dev.parhamziaei.teahub.enums.ticket.TicketStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public abstract class AbstractTicketResponse {

    @Schema(example = "0")
    protected Long id;

    protected TicketStatus status;

    protected TicketDepartment department;

}
