package dev.parhamziaei.teahub.dto.response.ticket.user;

import dev.parhamziaei.teahub.dto.response.ticket.AbstractTicketResponse;
import dev.parhamziaei.teahub.enums.ticket.TicketDepartment;
import dev.parhamziaei.teahub.enums.ticket.TicketStatus;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketListUserResponse extends AbstractTicketResponse {

    private String subject;

    private TicketStatus status;

    private TicketDepartment department;

    private LocalDateTime createdAt;

    private LocalDateTime lastModified;

}