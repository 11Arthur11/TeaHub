package dev.parhamziaei.teahub.service.mapper;

import dev.parhamziaei.teahub.dto.request.system.SystemNotificationRequest;
import dev.parhamziaei.teahub.dto.request.ticket.admin.TicketEditAdminRequest;
import dev.parhamziaei.teahub.entity.jpa.system.SystemNotification;
import dev.parhamziaei.teahub.entity.jpa.ticket.Ticket;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SystemNotificationMapStruct {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntity(SystemNotificationRequest editRequest, @MappingTarget SystemNotification notification);

}
