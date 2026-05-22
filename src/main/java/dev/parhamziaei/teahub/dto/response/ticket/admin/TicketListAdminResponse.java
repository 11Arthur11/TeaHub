package dev.parhamziaei.teahub.dto.response.ticket.admin;

import dev.parhamziaei.teahub.dto.response.ticket.AbstractTicketResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "'ownerId' field requires UserDetail Page redirect"
)
public class TicketListAdminResponse extends AbstractTicketResponse {

    private Long ownerId;

    private String ownerFullName;

    private String subject;

    private LocalDateTime createdAt;

    private LocalDateTime lastModified;

}
