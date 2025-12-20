package dev.parhamziaei.teahub.service.mapper.product;

import dev.parhamziaei.teahub.dto.request.shop.admin.TeaSpeakProductEditRequest;
import dev.parhamziaei.teahub.entity.jpa.shop.TeaSpeakProduct;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TeaSpeakProductMapStruct {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(TeaSpeakProductEditRequest request, @MappingTarget TeaSpeakProduct entity);

}
