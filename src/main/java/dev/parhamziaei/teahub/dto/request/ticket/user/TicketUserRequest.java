package dev.parhamziaei.teahub.dto.request.ticket.user;

import dev.parhamziaei.teahub.dto.request.ticket.TicketBaseRequest;
import dev.parhamziaei.teahub.enums.ticket.TicketDepartment;
import dev.parhamziaei.teahub.validation.annotation.EnumValue;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class TicketUserRequest implements TicketBaseRequest {

    @NotBlank
    @Length(min = 5)
    private String subject;

    private TicketDepartment department;

    private Long relatedResourceId;

    private TicketMessageRequest message;

    @Override
    public String getOwnerPhone() {
        return null;
    }
}

