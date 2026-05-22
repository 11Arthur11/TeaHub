package dev.parhamziaei.teahub.dto.request.ticket;

import dev.parhamziaei.teahub.dto.request.ticket.user.TicketUserRequest;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class TicketSubmitRequestDoc {

    @Schema(
            description = "Ticket payload",
            implementation = TicketUserRequest.class
    )
    public TicketUserRequest ticket;

    @ArraySchema(
            schema = @Schema(type = "string", format = "binary")
    )
    public List<MultipartFile> files;

}