package dev.parhamziaei.teahub.service.mapper;

import dev.parhamziaei.teahub.dto.request.ticket.admin.TicketEditAdminRequest;
import dev.parhamziaei.teahub.entity.jpa.ticket.Ticket;
import dev.parhamziaei.teahub.entity.jpa.ticket.TicketMessage;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TicketMapStruct {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntity(TicketEditAdminRequest editRequest, @MappingTarget Ticket ticket);

}
