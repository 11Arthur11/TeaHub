package dev.parhamziaei.teahub.service.interfaces;

import dev.parhamziaei.teahub.dto.internal.ImageInternal;
import dev.parhamziaei.teahub.dto.request.query.TicketFilterRequest;
import dev.parhamziaei.teahub.dto.request.ticket.TicketBaseRequest;
import dev.parhamziaei.teahub.dto.request.ticket.admin.TicketEditAdminRequest;
import dev.parhamziaei.teahub.dto.request.ticket.user.TicketMessageRequest;
import dev.parhamziaei.teahub.dto.response.ticket.AbstractTicketResponse;
import dev.parhamziaei.teahub.dto.response.ticket.admin.TicketListAdminResponse;
import dev.parhamziaei.teahub.dto.response.ticket.user.TicketDetailBaseResponse;
import dev.parhamziaei.teahub.enums.ticket.TicketStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TicketService {

    PagedModel<TicketListAdminResponse> getAllTickets(TicketFilterRequest filterRequest);
    void addNewMessage(TicketMessageRequest ticketMessageRequest, Long senderId, Long ticketId, List<MultipartFile> files);
    void changeTicketStatus(Long ticketId, TicketStatus newStatus);
    void editTicket(TicketEditAdminRequest request, Long ticketId);
    <T extends TicketDetailBaseResponse> T getTicketDetails(Long ticketId, Long requesterId, Class<T> responseType);
    <T extends TicketBaseRequest> void submit(Long submitterId, T ticketRequest, List<MultipartFile> files);
    <T extends AbstractTicketResponse> PagedModel<T> getUserTickets(TicketFilterRequest filterRequest, Long userId, Class<T> responseType);
    ImageInternal getTicketAttachment(String attachmentIdentifier, Long senderId);

}
