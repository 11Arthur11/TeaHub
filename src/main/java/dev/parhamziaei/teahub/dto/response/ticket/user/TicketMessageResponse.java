package dev.parhamziaei.teahub.dto.response.ticket.user;

import dev.parhamziaei.teahub.enums.user.Roles;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketMessageResponse {

    private String senderFullName;

    private LocalDateTime sentAt;

    private String message;

    private Roles senderRole;

    private Set<TicketAttachmentResponse> attachments;

}
