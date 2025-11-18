package dev.parhamziaei.teahub.dto.response.ticket.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketAttachmentResponse {

    private String attachmentName;

    private String identifier;

    private Long size;

}
