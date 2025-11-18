package dev.parhamziaei.teahub.controller.user;

import dev.parhamziaei.teahub.dto.internal.ImageInternal;
import dev.parhamziaei.teahub.component.CurrentUser;
import dev.parhamziaei.teahub.dto.request.query.TicketFilterRequest;
import dev.parhamziaei.teahub.dto.request.ticket.user.TicketMessageRequest;
import dev.parhamziaei.teahub.dto.request.ticket.user.TicketUserRequest;
import dev.parhamziaei.teahub.dto.response.ticket.user.TicketDetailBaseResponse;
import dev.parhamziaei.teahub.dto.response.ticket.user.TicketListUserResponse;
import dev.parhamziaei.teahub.enums.Message;
import dev.parhamziaei.teahub.exception.custom.service.NoSuchDataException;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.interfaces.JwtService;
import dev.parhamziaei.teahub.service.interfaces.TicketService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final JwtService jwtService;
    private final MessageService messageService;
    private final CurrentUser currentUser;

    @Operation(summary = "Getting all user tickets as list")
    @GetMapping
    public ResponseEntity<?> getTickets(@ModelAttribute TicketFilterRequest filterRequest) {
        final String userEmail = currentUser.getEmail();
        Pageable pageable = PageRequest.of(
                filterRequest.getPage(),
                filterRequest.getSize(),
                Sort.by(filterRequest.getSortedBy()).ascending()
        );

        PagedModel<TicketListUserResponse> userTickets = ticketService.getUserTickets(
                pageable,
                userEmail,
                TicketListUserResponse.class
        );

        if (userTickets.getContent().isEmpty()) {
            throw new NoSuchDataException();
        }

        return ResponseBuilder.buildSuccess(
                "DATA",
                userTickets,
                HttpStatus.OK
        );
    }

    @Operation(summary = "Submits ticket")
    @PostMapping(
            value = "/submit",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<?> submitTicket(
            @RequestPart("ticket") TicketUserRequest ticketRequest,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        if (files == null) {
            files = new ArrayList<>();
        }
        final String userEmail = currentUser.getEmail();
        ticketService.submit(userEmail, ticketRequest, files);
        return ResponseBuilder.buildSuccess(
                "SUCCESS",
                messageService.get(Message.SERVICE_TICKET_SUBMITTED),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Ticket Details with all messages")
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getTicketDetails(@PathVariable Long id) {
        final String userEmail = currentUser.getEmail();
        TicketDetailBaseResponse ticketDetails = ticketService.getTicketDetails(
                id,
                userEmail,
                TicketDetailBaseResponse.class
        );

        return ResponseBuilder.buildSuccess(
                "DATA",
                ticketDetails,
                HttpStatus.OK
        );
    }

    @Operation(summary = "Adding new message to existing ticket")
    @PutMapping(
            value = "/detail/{ticketId}/message",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<?> addTicketMessage(
            @PathVariable Long ticketId,
            @RequestPart("content") String content,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        final String userEmail = currentUser.getEmail();
        TicketMessageRequest ticketMessageRequest = new TicketMessageRequest(content);
        ticketService.addNewMessage(ticketMessageRequest, userEmail, ticketId, files);
        return ResponseBuilder.buildSuccess(
                "SUCCESS",
                messageService.get(Message.SERVICE_TICKET_MESSAGE_SENT),
                HttpStatus.OK
        );
    }

    @GetMapping("/attachment/{identifier}")
    public ResponseEntity<?> getAttachment(@PathVariable String identifier) {
        final String userEmail = currentUser.getEmail();
        ImageInternal image = ticketService.getTicketAttachment(identifier, userEmail);
        return ResponseBuilder.buildImageResponse(image);
    }

}
