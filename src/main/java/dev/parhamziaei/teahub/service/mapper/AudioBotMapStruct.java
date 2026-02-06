package dev.parhamziaei.teahub.service.mapper;

import dev.parhamziaei.teahub.dto.request.resource.user.EditAudioBotResourceRequest;
import dev.parhamziaei.teahub.dto.request.ticket.admin.TicketEditAdminRequest;
import dev.parhamziaei.teahub.entity.jpa.resource.AudioBotResource;
import dev.parhamziaei.teahub.entity.jpa.ticket.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AudioBotMapStruct {

    void toEntity(EditAudioBotResourceRequest editRequest, @MappingTarget AudioBotResource resource);

}
