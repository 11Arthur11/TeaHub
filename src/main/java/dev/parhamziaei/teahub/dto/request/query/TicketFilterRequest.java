package dev.parhamziaei.teahub.dto.request.query;

import dev.parhamziaei.teahub.enums.ticket.TicketDepartment;
import dev.parhamziaei.teahub.enums.ticket.TicketStatus;
import dev.parhamziaei.teahub.validation.annotation.EnumValue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TicketFilterRequest extends AbstractPaginationRequest {

    protected TicketStatus status;

    protected TicketDepartment department;

    protected String sortedBy = "createdAt";
}


