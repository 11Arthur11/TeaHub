package dev.parhamziaei.teahub.dto.response.ticket.user;

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
public class TicketListUserResponse extends AbstractTicketResponse {

    private String subject;

    private String status;

    private String department;

    private LocalDateTime createdAt;

    private LocalDateTime lastModified;

}