package dev.parhamziaei.teahub.controller.admin;

import dev.parhamziaei.teahub.component.CurrentUser;
import dev.parhamziaei.teahub.dto.request.query.TicketFilterRequest;
import dev.parhamziaei.teahub.dto.request.ticket.admin.TicketAdminRequest;
import dev.parhamziaei.teahub.dto.request.ticket.admin.TicketEditAdminRequest;
import dev.parhamziaei.teahub.dto.response.ticket.admin.TicketDetailAdminResponse;
import dev.parhamziaei.teahub.dto.response.ticket.admin.TicketListAdminResponse;
import dev.parhamziaei.teahub.enums.Message;
import dev.parhamziaei.teahub.exception.custom.service.NoSuchDataException;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.interfaces.TicketService;
import dev.parhamziaei.teahub.utils.PhoneNumbers;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
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

    @GetMapping
    public ResponseEntity<?> getAllTickets(
            @ModelAttribute @Valid TicketFilterRequest filterRequest
    ) {
        PagedModel<TicketListAdminResponse> tickets = ticketService.getAllTickets(filterRequest);
        if (tickets.getContent().isEmpty()) {
            throw new NoSuchDataException();
        }
        return ResponseBuilder.buildSuccess(
                "DATA",
                tickets,
                HttpStatus.OK
        );
    }

    @GetMapping("/{phoneNumber}")
    public ResponseEntity<?> getAllUserTickets(
            @ModelAttribute TicketFilterRequest filterRequest,
            @PathVariable String phoneNumber
    ) {
        phoneNumber = PhoneNumbers.formatedOf(phoneNumber);
        Pageable pageable = PageRequest.of(filterRequest.getPage(), filterRequest.getSize());
        PagedModel<TicketListAdminResponse> tickets = ticketService.getUserTickets(
                pageable,
                phoneNumber,
                TicketListAdminResponse.class
        );
        if (tickets.getContent().isEmpty()) {
            throw new NoSuchDataException();
        }
        return ResponseBuilder.buildSuccess(
                "DATA",
                tickets,
                HttpStatus.OK
        );
    }

    @PutMapping("/edit/{ticketId}")
    public ResponseEntity<?> editTicket(
            @Valid @RequestBody TicketEditAdminRequest editRequest,
            @PathVariable Long ticketId
    ) {
        ticketService.editTicket(editRequest, ticketId);
        return ResponseBuilder.buildSuccess(
                "SUCCESS",
                messageService.get(Message.SERVICE_TICKET_EDITED),
                HttpStatus.OK
        );
    }

    @GetMapping("/detail/{ticketId}")
    public ResponseEntity<?> getTicketDetails(@PathVariable Long ticketId){
        String phoneNumber = currentUser.getPhone();
        TicketDetailAdminResponse ticket = ticketService.getTicketDetails(
                ticketId,
                phoneNumber,
                TicketDetailAdminResponse.class
        );

        return ResponseBuilder.buildSuccess(
                "DATA",
                ticket,
                HttpStatus.OK
        );
    }

    @Operation(summary = "Submits ticket")
    @PostMapping(
            value = "/submit",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<?> submitTicket(
            @RequestPart("ticket") TicketAdminRequest ticketRequest,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        final String submitterPhone = currentUser.getPhone();
        ticketService.submit(submitterPhone, ticketRequest, files);
        return ResponseBuilder.buildSuccess(
                "SUCCESS",
                messageService.get(Message.SERVICE_TICKET_SUBMITTED),
                HttpStatus.OK
        );
    }
}
