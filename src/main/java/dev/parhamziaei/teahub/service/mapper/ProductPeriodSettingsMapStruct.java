package dev.parhamziaei.teahub.service.mapper;

import dev.parhamziaei.teahub.valueobject.ProductPeriodSettings;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductPeriodSettingsMapStruct {

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    void update(
            ProductPeriodSettings source,
            @MappingTarget ProductPeriodSettings target
    );
}
