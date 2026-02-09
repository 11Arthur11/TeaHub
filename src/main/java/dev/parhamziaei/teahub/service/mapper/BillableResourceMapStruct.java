package dev.parhamziaei.teahub.service.mapper;

import dev.parhamziaei.teahub.dto.request.audio_bot.admin.AudioBotNodeEditRequest;
import dev.parhamziaei.teahub.dto.request.resource.user.AudioBotResourceEditRequest;
import dev.parhamziaei.teahub.dto.request.resource.user.BillableResourceEditRequest;
import dev.parhamziaei.teahub.entity.jpa.audio_bot.AudioBotNode;
import dev.parhamziaei.teahub.entity.jpa.resource.AudioBotResource;
import dev.parhamziaei.teahub.entity.jpa.resource.BillableResource;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BillableResourceMapStruct {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntity(BillableResourceEditRequest editRequest, @MappingTarget BillableResource resource);

}
