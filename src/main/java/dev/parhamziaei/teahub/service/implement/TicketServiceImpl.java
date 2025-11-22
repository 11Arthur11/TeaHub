package dev.parhamziaei.teahub.service.implement;

import dev.parhamziaei.teahub.configuration.properties.TicketServiceProperties;
import dev.parhamziaei.teahub.dto.internal.ImageInternal;
import dev.parhamziaei.teahub.dto.request.query.TicketFilterRequest;
import dev.parhamziaei.teahub.dto.request.ticket.TicketBaseRequest;
import dev.parhamziaei.teahub.dto.request.ticket.admin.TicketEditAdminRequest;
import dev.parhamziaei.teahub.dto.request.ticket.user.TicketMessageRequest;
import dev.parhamziaei.teahub.dto.response.ticket.AbstractTicketResponse;
import dev.parhamziaei.teahub.dto.response.ticket.admin.TicketListAdminResponse;
import dev.parhamziaei.teahub.dto.response.ticket.user.TicketAttachmentResponse;
import dev.parhamziaei.teahub.dto.response.ticket.user.TicketDetailBaseResponse;
import dev.parhamziaei.teahub.dto.response.ticket.user.TicketMessageResponse;
import dev.parhamziaei.teahub.entity.jpa.ticket.Ticket;
import dev.parhamziaei.teahub.entity.jpa.ticket.TicketMessage;
import dev.parhamziaei.teahub.entity.jpa.ticket.TicketMessageAttachment;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.TicketDepartment;
import dev.parhamziaei.teahub.enums.TicketStatus;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchDataException;
import dev.parhamziaei.teahub.exception.custom.service.ticket.TicketMaxAttachmentReachedException;
import dev.parhamziaei.teahub.exception.custom.service.ticket.TicketServiceException;
import dev.parhamziaei.teahub.repository.jpa.TicketRepo;
import dev.parhamziaei.teahub.service.FileStorageService;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.interfaces.TicketService;
import dev.parhamziaei.teahub.service.interfaces.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.*;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

// reminder make sure you send an email or sms on each ticket sending or modified phase.

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepo ticketRepo;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final MessageService messageService;
    private final TicketServiceProperties properties;

    protected BiPredicate<User, Ticket> hasAccessToTicket = (user, ticket) -> {
        if (user.isStaff())
            return true;
        else
            return ticket.getOwnerPhone().equals(user.getPhone());
    };

    protected BiPredicate<User, TicketMessageAttachment> hasAccessToAttachment = (user, attachment) -> {
        if (user.isStaff())
            return true;
        else
            return attachment.getOwnerPhone().equals(user.getPhone());
    };

    private <T extends AbstractTicketResponse> T enrichTicket(Ticket ticket, T dto) {
        dto.setDepartment(messageService.get(TicketDepartment.fromValue(ticket.getDepartment())));
        dto.setStatus(messageService.get(TicketStatus.fromValue(ticket.getStatus())));
        return dto;
    }

    private <T extends AbstractTicketResponse> Page<T> mapPage(Page<Ticket> source, Class<T> responseType) {
        List<T> list = new ArrayList<>();
        source.getContent().forEach(t -> {
            T dto = enrichTicket(t, modelMapper.map(t, responseType));
            list.add(dto);
        });
        return new PageImpl<>(list, source.getPageable(), source.getTotalElements());
    }

    private <T extends AbstractTicketResponse> T mapTicket(Ticket ticket, Class<T> responseType) {
        return enrichTicket(ticket, modelMapper.map(ticket, responseType));
    }

    private <T extends TicketDetailBaseResponse> T mapTicketDetail(Ticket ticket, Class<T> responseType) {
        List<TicketMessageResponse> messagesDTO = mapMessages(ticket.getMessages());
        T dto = enrichTicket(ticket, modelMapper.map(ticket, responseType));
        dto.setMessages(messagesDTO);
        return dto;
    }

    private List<TicketMessageResponse> mapMessages(Set<TicketMessage> ticketMessage) {
        List<TicketMessageResponse> messagesDTO = new ArrayList<>();

        ticketMessage.forEach(tm -> {
            Set<TicketAttachmentResponse> attachmentsDTO = tm.getAttachments()
                    .stream()
                    .map(att ->
                            TicketAttachmentResponse.builder()
                                    .attachmentName(att.getOriginalName())
                                    .size(att.getSize())
                                    .identifier(att.getStoredName())
                                    .build()
                    )
                    .collect(Collectors.toSet());

            TicketMessageResponse messageDTO = modelMapper.map(tm, TicketMessageResponse.class);
            messageDTO.setAttachments(attachmentsDTO);
            messagesDTO.add(messageDTO);
        });

        return messagesDTO.stream()
                .sorted(Comparator.comparing(TicketMessageResponse::getSentAt))
                .toList();
    }

    @Override
    public PagedModel<TicketListAdminResponse> getAllTickets(TicketFilterRequest filterRequest) {
        Pageable pageable = PageRequest.of(filterRequest.getPage(), filterRequest.getSize(), Sort.by(filterRequest.getSortedBy()).ascending());
        Page<Ticket> ticketPage;

        boolean isAllStatus = filterRequest.getStatus() == null;
        boolean isAllDepartment = filterRequest.getDepartment() == null;

        if (isAllStatus && isAllDepartment) {
            ticketPage = ticketRepo.findAll(pageable);
        } else if (isAllStatus) {
            TicketDepartment department = TicketDepartment.fromValue(filterRequest.getDepartment());
            ticketPage = ticketRepo.findAllByDepartment(department, pageable);
        } else if (isAllDepartment) {
            TicketStatus status = TicketStatus.fromValue(filterRequest.getStatus());
            ticketPage = ticketRepo.findAllByStatus(status, pageable);
        } else {
            TicketDepartment department = TicketDepartment.fromValue(filterRequest.getDepartment());
            TicketStatus status = TicketStatus.fromValue(filterRequest.getStatus());
            ticketPage = ticketRepo.findAllByStatusAndDepartment(status, department, pageable);
        }

        Page<TicketListAdminResponse> dtoPage = mapPage(ticketPage, TicketListAdminResponse.class);
        return new PagedModel<>(dtoPage);
    }

    @Override
    @Transactional
    public void addNewMessage(TicketMessageRequest ticketMessageRequest, String senderUserPhone, Long ticketId, List<MultipartFile> files) {
        User user = (User) userService.loadUserByUsername(senderUserPhone);
        addNewMessage(ticketMessageRequest, user, ticketId, files);
    }

    protected BiFunction<TicketStatus, User, TicketStatus> calculateNewStatus = (currentStatus, modifierUser) -> {
        switch (currentStatus) {
            case CLOSED -> {
                if (!modifierUser.isStaff())
                    throw new TicketServiceException("Cannot add new message on closed ticket");
                else
                    return TicketStatus.WAITING;
            }
            case PENDING -> {
                if (modifierUser.isStaff())
                    return TicketStatus.WAITING;
            }
            case RESPONDED, WAITING -> {
                if (!modifierUser.isStaff())
                    return TicketStatus.PENDING;
            }
        }
        return currentStatus;
    };

    @Override
    public void changeTicketStatus(Long ticketId, TicketStatus newStatus) {
        Optional<Ticket> dbTicket = ticketRepo.findById(ticketId);
        if (dbTicket.isPresent()) {
            Ticket ticket = dbTicket.get();
            ticket.setStatus(newStatus.value());
            ticketRepo.update(ticket);
        }
    }

    @Override
    public void editTicket(TicketEditAdminRequest request, Long ticketId) {
        Optional<Ticket> dbTicket = ticketRepo.findById(ticketId);
        if (dbTicket.isPresent()) {
            Ticket ticket = dbTicket.get();

            if (request.getNewSubject() != null)
                ticket.setSubject(request.getNewSubject());
            if (request.getNewStatus() != null)
                ticket.setStatus(request.getNewStatus());
            if (ticket.getStatus() != null)
                ticket.setStatus(ticket.getStatus());

            ticketRepo.update(ticket);
        }
    }

    @Transactional
    protected void addNewMessage(TicketMessageRequest ticketMessageRequest, User senderUser, Long ticketId, List<MultipartFile> files) {
        if (files != null && files.size() > properties.maxAttachmentPerMessage())  {
            throw new TicketMaxAttachmentReachedException("Maximum number of ticket attachments reached. limit is: " + properties.maxAttachmentPerMessage());
        }
        Optional<Ticket> loadedTicket = ticketRepo.findById(ticketId);
        if (loadedTicket.isPresent() && hasAccessToTicket.test(senderUser, loadedTicket.get())) {
            Ticket ticket = loadedTicket.get();

            String senderRole = senderUser.getHigherAuthority().getName();
            TicketStatus newStatus = calculateNewStatus.apply(
                    TicketStatus.fromValue(ticket.getStatus()),
                    senderUser
            );

            if (!newStatus.equals(TicketStatus.fromValue(ticket.getStatus()))) {
                changeTicketStatus(ticket.getId(), newStatus);
            }

            TicketMessage newTicketMessage = TicketMessage.builder()
                    .message(ticketMessageRequest.getContent())
                    .senderFullName(senderUser.getFullName())
                    .senderRole(senderRole)
                    .build();

            addAttachmentsToTicketMessage(
                    ticketRepo.addMessage(ticketId, newTicketMessage),
                    files
            );

        } else {
            throw new TicketServiceException("Ticket not found with id " + ticketId);
        }
    }

    @Transactional
    protected void addAttachmentsToTicketMessage(Optional<TicketMessage> ticketMessage, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return;
        }
        ticketMessage.ifPresent(loadedTicketMessage -> files.forEach(file -> {
            String storedPath = fileStorageService.storeTicketAttachment(file);

            TicketMessageAttachment attachment = TicketMessageAttachment.builder()
                    .originalName(file.getOriginalFilename())
                    .storedPath(storedPath)
                    .storedName(Paths.get(storedPath).getFileName().toString())
                    .size(file.getSize())
                    .mimeType(file.getContentType())
                    .ownerPhone(loadedTicketMessage.getTicket().getOwnerPhone())
                    .ticketMessage(loadedTicketMessage)
                    .build();

            loadedTicketMessage.addAttachment(attachment);
        }));
    }

    @Override
    public <T extends TicketDetailBaseResponse> T getTicketDetails(Long ticketId, String requesterPhone, Class<T> responseType) {
        User requesterUser = (User) userService.loadUserByUsername(requesterPhone);
        Optional<Ticket> loadedTicket = ticketRepo.findById(ticketId);
        if (loadedTicket.isPresent() && hasAccessToTicket.test(requesterUser, loadedTicket.get())) {
            Ticket ticket = loadedTicket.get();
            return mapTicketDetail(ticket, responseType);
        } else {
            throw new NoSuchDataException("Ticket not found with id " + ticketId);
        }
    }

    @Override
    @Transactional
    public <T extends TicketBaseRequest> void submit(String submitterPhoneNumber, T ticketRequest, List<MultipartFile> files) {
        String relatedServiceName = null;
        User submitterUser = (User) userService.loadUserByUsername(submitterPhoneNumber);
        if (ticketRequest.getServiceName() != null) {
            //todo make a check if entered service belong to user or not, if not throw TicketServiceException()
        }
        String ticketDepartment = ticketRequest.getDepartment();

        Ticket ticket = Ticket.builder()
                .subject(ticketRequest.getSubject())
                .department(ticketDepartment)
                .serviceName(relatedServiceName)
                .submitterPhone(submitterUser.getPhone())
                .ownerFullName(submitterUser.getFullName())
                .build();

        if (submitterUser.isStaff()) {
            ticket.setStatus(TicketStatus.WAITING.value());
            ticket.setOwnerPhone(ticketRequest.getOwnerPhone());
        } else {
            ticket.setStatus(TicketStatus.PENDING.value());
            ticket.setOwnerPhone(submitterPhoneNumber);
        }

        ticketRepo.save(ticket);
        addNewMessage(
                ticketRequest.getMessage(),
                submitterUser,
                ticket.getId(),
                files
        );

    }

    @Override
    public <T extends AbstractTicketResponse> PagedModel<T> getUserTickets(Pageable pageable, String phoneNumber, Class<T> responseType) {
        Page<Ticket> tickets = ticketRepo.findByOwner(pageable, phoneNumber);
        Page<T> sortedPageDTO = mapPage(tickets, responseType);
        return new PagedModel<>(sortedPageDTO);
    }

    @Override
    public ImageInternal getTicketAttachment(String attachmentIdentifier, String senderPhone) {
        Optional<TicketMessageAttachment> dbAttachment = ticketRepo.findTicketAttachmentByStoredName(attachmentIdentifier);
        User user = (User) userService.loadUserByUsername(senderPhone);
        if (dbAttachment.isPresent()) {
            TicketMessageAttachment attachment = dbAttachment.get();
            Optional<Resource> imageResource = fileStorageService.loadTicketAttachment(attachment);
            if (hasAccessToAttachment.test(user, attachment) && imageResource.isPresent()) {
                return ImageInternal.builder()
                        .image(imageResource.get())
                        .originalName(attachment.getOriginalName())
                        .size(attachment.getSize())
                        .mimeType(attachment.getMimeType())
                        .storedName(attachment.getStoredName())
                        .build();
            }
        }
        log.warn("Ticket attachment not found or permission missing, user: {}, fileName: {}", senderPhone, attachmentIdentifier);
        throw new TicketServiceException("Attachment not found or permission denied.");
    }
}
