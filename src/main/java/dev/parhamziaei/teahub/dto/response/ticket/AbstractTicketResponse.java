package dev.parhamziaei.teahub.dto.response.ticket;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public abstract class AbstractTicketResponse {

    @Schema(example = "0")
    protected Long id;

    protected String status;

    protected String department;

}
