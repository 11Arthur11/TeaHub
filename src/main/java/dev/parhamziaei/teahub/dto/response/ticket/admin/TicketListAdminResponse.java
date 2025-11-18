package dev.parhamziaei.teahub.dto.response.ticket.admin;

import dev.parhamziaei.teahub.dto.response.ticket.AbstractTicketResponse;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketListAdminResponse extends AbstractTicketResponse {

    private String ownerPhone;

    private String ownerFullName;

    private String subject;

    private LocalDateTime createdAt;

    private LocalDateTime lastModified;

}
