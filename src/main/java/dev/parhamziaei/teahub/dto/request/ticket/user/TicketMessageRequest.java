package dev.parhamziaei.teahub.dto.request.ticket.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class TicketMessageRequest {

    private String content;

}
