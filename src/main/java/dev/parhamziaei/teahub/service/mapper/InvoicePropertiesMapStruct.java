package dev.parhamziaei.teahub.service.mapper;

import dev.parhamziaei.teahub.valueobject.InvoiceProperties;
import dev.parhamziaei.teahub.valueobject.ProductPeriodSettings;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InvoicePropertiesMapStruct {

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    void update(
            InvoiceProperties source,
            @MappingTarget InvoiceProperties target
    );

}
