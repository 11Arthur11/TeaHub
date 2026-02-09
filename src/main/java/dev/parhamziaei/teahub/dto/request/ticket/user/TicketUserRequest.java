package dev.parhamziaei.teahub.dto.request.ticket.user;

import dev.parhamziaei.teahub.dto.request.ticket.AbstractTicketRequest;
import dev.parhamziaei.teahub.enums.ticket.TicketDepartment;
import dev.parhamziaei.teahub.validation.annotation.EnumValue;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

@EqualsAndHashCode(callSuper = true)
@Data
public class TicketUserRequest extends AbstractTicketRequest {



}

