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
import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.entity.jpa.ticket.Ticket;
import dev.parhamziaei.teahub.entity.jpa.ticket.TicketMessage;
import dev.parhamziaei.teahub.entity.jpa.ticket.TicketMessageAttachment;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.ticket.TicketStatus;
import dev.parhamziaei.teahub.enums.user.Roles;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchDataException;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.exception.custom.service.ticket.TicketMaxAttachmentReachedException;
import dev.parhamziaei.teahub.exception.custom.service.ticket.TicketServiceException;
import dev.parhamziaei.teahub.repository.jpa.BillableResourceRepository;
import dev.parhamziaei.teahub.repository.jpa.TicketRepository;
import dev.parhamziaei.teahub.repository.jpa.UserRepository;
import dev.parhamziaei.teahub.repository.jpa.specification.TicketSpecification;
import dev.parhamziaei.teahub.service.FileStorageService;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.interfaces.TicketService;
import dev.parhamziaei.teahub.service.interfaces.UserService;
import dev.parhamziaei.teahub.service.mapper.TicketMapStruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
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

    private final TicketRepository ticketRepo;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final MessageService messageService;
    private final TicketServiceProperties properties;
    private final UserRepository userRepository;
    private final BillableResourceRepository billableResourceRepository;
    private final TicketMapStruct ticketMapStruct;

    protected BiPredicate<User, TicketMessageAttachment> hasAccessToAttachment = (user, attachment) -> {
        if (user.isStaff())
            return true;
        else
            return attachment.getTicketMessage()
                    .getTicket()
                    .getOwner()
                    .getPhone()
                    .equals(user.getPhone());
    };

    private <T extends AbstractTicketResponse> T enrichTicket(Ticket ticket, T dto) {
        dto.setDepartment(messageService.get(ticket.getDepartment()));
        dto.setStatus(messageService.get(ticket.getDepartment()));
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

        Specification<Ticket> spec = TicketSpecification.hasDepartment(filterRequest.getDepartment())
                .and(TicketSpecification.hasStatus(filterRequest.getStatus()));

        Page<Ticket> page = ticketRepo.findAll(spec, pageable);

        if (page.getContent().isEmpty())
            throw new NoSuchDataException();

        Page<TicketListAdminResponse> dtoPage = mapPage(page, TicketListAdminResponse.class);
        return new PagedModel<>(dtoPage);
    }

    @Override
    @Transactional
    public void addNewMessage(TicketMessageRequest ticketMessageRequest, Long senderId, Long ticketId, List<MultipartFile> files) {
        User user = userRepository.findById(senderId)
                .orElseThrow(NoSuchEntityException::new);
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
        Ticket ticket = ticketRepo.findById(ticketId)
                .orElseThrow(NoSuchEntityException::new);
        ticket.setStatus(newStatus);
        ticketRepo.saveAndFlush(ticket);
    }

    @Override
    public void editTicket(TicketEditAdminRequest request, Long ticketId) {
        Ticket ticket = ticketRepo.findById(ticketId)
                .orElseThrow(NoSuchEntityException::new);
        ticketMapStruct.toEntity(request, ticket);
        ticketRepo.saveAndFlush(ticket);
    }

    @Transactional
    protected void addNewMessage(TicketMessageRequest ticketMessageRequest, User senderUser, Long ticketId, List<MultipartFile> files) {
        if (files != null && files.size() > properties.maxAttachmentPerMessage())  {
            throw new TicketMaxAttachmentReachedException("Maximum number of ticket attachments reached. limit is: " + properties.maxAttachmentPerMessage());
        }

        Ticket ticket = ticketRepo.findByOneByPermission(senderUser, ticketId)
                .orElseThrow(() -> new TicketServiceException("Ticket not found with id " + ticketId));

        String senderRole = messageService.get(Roles.fromName(senderUser.getHigherAuthority().getName()));
        TicketStatus newStatus = calculateNewStatus.apply(
                ticket.getStatus(),
                senderUser
        );

        if (!newStatus.equals(ticket.getStatus())) {
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
                    .ticketMessage(loadedTicketMessage)
                    .build();

            loadedTicketMessage.addAttachment(attachment);
        }));
    }

    @Override
    public <T extends TicketDetailBaseResponse> T getTicketDetails(Long ticketId, Long requesterId, Class<T> responseType) {
        User requesterUser = userRepository.findById(requesterId)
                .orElseThrow(NoSuchEntityException::new);

        Ticket ticket = ticketRepo.findByOneByPermission(requesterUser, ticketId)
                .orElseThrow(() -> new NoSuchDataException("Ticket not found with id " + ticketId));

        return mapTicketDetail(ticket, responseType);
    }

    @Override
    @Transactional
    public <T extends TicketBaseRequest> void submit(Long submitterId, T ticketRequest, List<MultipartFile> files) {
        Long relatedResourceId = null;

        User submitterUser = userRepository.findById(submitterId)
                .orElseThrow(NoSuchDataException::new);

        if (ticketRequest.getRelatedResourceId() != null) {
            BillableResource resource = billableResourceRepository.findByOneByPermission(submitterUser, ticketRequest.getRelatedResourceId())
                    .orElseThrow(NoSuchDataException::new);
            relatedResourceId = resource.getId();
        }

        Ticket ticket = Ticket.builder()
                .subject(ticketRequest.getSubject())
                .department(ticketRequest.getDepartment())
                .relatedResourceId(relatedResourceId)
                .build();

        ticket.setOwner(submitterUser);

        if (submitterUser.isStaff()) {
            ticket.setStatus(TicketStatus.WAITING);
            User ownerUser = userService.loadUserByPhoneNumber(ticketRequest.getOwnerPhone());
            ticket.setOwner(ownerUser);
        } else {
            ticket.setStatus(TicketStatus.PENDING);
            ticket.setOwner(submitterUser);
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
    public <T extends AbstractTicketResponse> PagedModel<T> getUserTickets(TicketFilterRequest filterRequest, Long userId, Class<T> responseType) {
        User user = userRepository.findById(userId)
                .orElseThrow(NoSuchEntityException::new);

        Pageable pageable = PageRequest.of(
                filterRequest.getPage(),
                filterRequest.getSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Specification<Ticket> spec = TicketSpecification.hasDepartment(filterRequest.getDepartment())
                .and(TicketSpecification.hasStatus(filterRequest.getStatus()))
                .and(TicketSpecification.mustHaveAccess(user));

        Page<Ticket> tickets = ticketRepo.findAll(spec, pageable);

        if (tickets.getContent().isEmpty())
            throw new NoSuchDataException();

        Page<T> sortedPageDTO = mapPage(tickets, responseType);
        return new PagedModel<>(sortedPageDTO);
    }

    @Override
    public ImageInternal getTicketAttachment(String attachmentIdentifier, Long requesterId) {
        Optional<TicketMessageAttachment> dbAttachment = ticketRepo.findTicketAttachmentByStoredName(attachmentIdentifier);
        User user = userRepository.findById(requesterId)
                .orElseThrow(NoSuchDataException::new);
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
        log.warn("Ticket attachment not found or permission missing, user: {}, fileName: {}", user.getPhone(), attachmentIdentifier);
        throw new TicketServiceException("Attachment not found or permission denied.");
    }
}
