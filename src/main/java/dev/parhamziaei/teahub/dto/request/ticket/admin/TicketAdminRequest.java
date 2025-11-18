package dev.parhamziaei.teahub.dto.request.ticket.admin;

import dev.parhamziaei.teahub.dto.request.ticket.TicketBaseRequest;
import dev.parhamziaei.teahub.dto.request.ticket.user.TicketMessageRequest;
import dev.parhamziaei.teahub.enums.TicketDepartment;
import dev.parhamziaei.teahub.validation.annotation.EnumValue;
import dev.parhamziaei.teahub.validation.annotation.PhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class TicketAdminRequest implements TicketBaseRequest {

    @NotBlank
    @Length(min = 5)
    private String subject;

    @EnumValue(enumClass = TicketDepartment.class)
    private String department;

    private String serviceName;

    @PhoneNumber
    private String ownerPhone;

    private TicketMessageRequest message;

}
