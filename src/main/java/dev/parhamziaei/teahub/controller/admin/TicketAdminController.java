package dev.parhamziaei.teahub.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.parhamziaei.teahub.component.CurrentUser;
import dev.parhamziaei.teahub.dto.request.query.TicketFilterRequest;
import dev.parhamziaei.teahub.dto.request.ticket.TicketSubmitRequestDoc;
import dev.parhamziaei.teahub.dto.request.ticket.admin.TicketAdminRequest;
import dev.parhamziaei.teahub.dto.request.ticket.admin.TicketEditAdminRequest;
import dev.parhamziaei.teahub.dto.response.global.DataResponse;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.dto.response.ticket.admin.TicketDetailAdminResponse;
import dev.parhamziaei.teahub.dto.response.ticket.admin.TicketListAdminResponse;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchDataException;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.interfaces.TicketService;
import dev.parhamziaei.teahub.utils.PhoneNumbers;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/tickets")
public class TicketAdminController {

    private final TicketService ticketService;
    private final CurrentUser currentUser;
    private final MessageService messageService;
    private final ObjectMapper objectMapper;

    @Operation(
            summary = "Get all tickets",
            description = "Returns a paginated list of all tickets in the system. " +
                    "You can use TicketFilterRequest to filter, sort, and paginate results.",
            tags = {"Ticket (Admin)"}
    )
    @GetMapping
    public ResponseEntity<DataResponse<PagedModel<TicketListAdminResponse>>> getAllTickets(
            @ModelAttribute @Valid TicketFilterRequest filterRequest
    ) {
        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                ticketService.getAllTickets(filterRequest),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Get all tickets for a user",
            description = "Returns a paginated list of tickets for a specific user identified by phone number. " +
                    "Filters and pagination can be applied via TicketFilterRequest.",
            tags = {"Ticket (Admin)"}
    )
    @GetMapping("/{userId}")
    public ResponseEntity<DataResponse<PagedModel<TicketListAdminResponse>>> getAllUserTickets(
            @ModelAttribute TicketFilterRequest filterRequest,
            @PathVariable Long userId
    ) {
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                ticketService.getUserTickets(
                        filterRequest,
                        userId,
                        TicketListAdminResponse.class
                ),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Edit a ticket",
            description = "Allows admin to edit an existing ticket identified by ticketId. " +
                    "Send updated data in TicketEditAdminRequest.",
            tags = {"Ticket (Admin)"}
    )
    @PutMapping("/edit/{ticketId}")
    public ResponseEntity<SimpleResponse> editTicket(
            @Valid @RequestBody TicketEditAdminRequest editRequest,
            @PathVariable Long ticketId
    ) {
        ticketService.editTicket(editRequest, ticketId);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.TICKET_EDITED),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Get ticket details",
            description = "Returns detailed information for a single ticket, including all messages. " +
                    "Ticket is identified by ticketId.",
            tags = {"Ticket (Admin)"}
    )
    @GetMapping("/detail/{ticketId}")
    public ResponseEntity<DataResponse<TicketDetailAdminResponse>> getTicketDetails(@PathVariable Long ticketId){
        TicketDetailAdminResponse ticket = ticketService.getTicketDetails(
                ticketId,
                currentUser.getId(),
                TicketDetailAdminResponse.class
        );

        return ResponseBuilder.buildSuccess(
                ResponseType.DATA,
                ticket,
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Submit a new ticket",
            description = "Allows admin to submit a new ticket with optional file attachments. " +
                    "Ticket data is sent as multipart/form-data with 'ticket' and 'files' fields.",
            tags = {"Ticket (Admin)"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = TicketSubmitRequestDoc.class)
                    )
            )
    )
    @PostMapping(
            value = "/submit",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<SimpleResponse> submitTicket(
            @RequestPart("ticket") String ticketString,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws JsonProcessingException {
        TicketAdminRequest ticketRequest = objectMapper.readValue(ticketString, TicketAdminRequest.class);
        ticketRequest.getMessage().setFiles(files);
        ticketService.submit(currentUser.getId(), ticketRequest);
        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(ServiceMessage.TICKET_SUBMITTED),
                HttpStatus.OK
        );
    }
}
