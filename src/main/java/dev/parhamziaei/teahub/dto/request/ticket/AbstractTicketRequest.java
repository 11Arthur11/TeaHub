package dev.parhamziaei.teahub.dto.request.ticket;

import dev.parhamziaei.teahub.dto.request.ticket.user.TicketMessageRequest;
import dev.parhamziaei.teahub.enums.ticket.TicketDepartment;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public abstract class AbstractTicketRequest {

    @NotBlank
    @Length(min = 5)
    private String subject;

    private TicketDepartment department;

    private Long relatedResourceId;

    private TicketMessageRequest message;

}
