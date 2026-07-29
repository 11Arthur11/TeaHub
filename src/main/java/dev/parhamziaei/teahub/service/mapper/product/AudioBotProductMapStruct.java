package dev.parhamziaei.teahub.service.mapper.product;

import dev.parhamziaei.teahub.dto.request.shop.admin.AudioBotProductEditRequest;
import dev.parhamziaei.teahub.dto.request.shop.admin.TeaSpeakProductEditRequest;
import dev.parhamziaei.teahub.entity.jpa.shop.AudioBotProduct;
import dev.parhamziaei.teahub.entity.jpa.shop.TeaSpeakProduct;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AudioBotProductMapStruct {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(AudioBotProductEditRequest request, @MappingTarget AudioBotProduct entity);

}
