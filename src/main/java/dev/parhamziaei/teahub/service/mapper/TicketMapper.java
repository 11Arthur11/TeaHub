package dev.parhamziaei.teahub.service.mapper;

import dev.parhamziaei.teahub.dto.response.ticket.AbstractTicketResponse;
import dev.parhamziaei.teahub.dto.response.ticket.user.TicketAttachmentResponse;
import dev.parhamziaei.teahub.dto.response.ticket.user.TicketDetailBaseResponse;
import dev.parhamziaei.teahub.dto.response.ticket.user.TicketMessageResponse;
import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import dev.parhamziaei.teahub.entity.jpa.ticket.Ticket;
import dev.parhamziaei.teahub.entity.jpa.ticket.TicketMessage;
import dev.parhamziaei.teahub.repository.jpa.BillableResourceRepository;
import dev.parhamziaei.teahub.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TicketMapper {

    private final ModelMapper modelMapper;
    private final BillableResourceRepository billableResourceRepository;

    public <T extends AbstractTicketResponse> Page<T> mapPage(Page<Ticket> source, Class<T> responseType) {
        List<T> list = new ArrayList<>();
        source.getContent().forEach(t -> modelMapper.map(t, responseType));
        return new PageImpl<>(list, source.getPageable(), source.getTotalElements());
    }

    public <T extends TicketDetailBaseResponse> T mapTicketDetail(Ticket ticket, Class<T> responseType) {
        List<TicketMessageResponse> messagesDTO = mapMessages(ticket.getMessages());
        T dto = modelMapper.map(ticket, responseType);
        dto.setMessages(messagesDTO);

        if (ticket.getRelatedResourceId() != null)
            billableResourceRepository.findById(ticket.getRelatedResourceId())
                    .ifPresent(r -> dto.setServiceName(r.getFormattedName()));
        return dto;
    }

    public List<TicketMessageResponse> mapMessages(Set<TicketMessage> ticketMessage) {
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

}
