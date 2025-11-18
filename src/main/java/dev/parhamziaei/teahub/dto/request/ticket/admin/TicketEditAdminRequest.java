package dev.parhamziaei.teahub.dto.request.ticket.admin;

import dev.parhamziaei.teahub.enums.TicketDepartment;
import dev.parhamziaei.teahub.enums.TicketStatus;
import dev.parhamziaei.teahub.validation.annotation.EnumValue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@NotNull
public class TicketEditAdminRequest {

    @Length(min = 5)
    private String newSubject;

    @EnumValue(enumClass = TicketStatus.class, allowNull = true)
    private String newStatus;

    @EnumValue(enumClass = TicketDepartment.class, allowNull = true)
    private String newDepartment;

}
