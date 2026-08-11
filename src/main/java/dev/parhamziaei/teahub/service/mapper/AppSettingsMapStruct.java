package dev.parhamziaei.teahub.service.mapper;

import dev.parhamziaei.teahub.dto.request.payment.admin.AqayePardakhtPersistRequest;
import dev.parhamziaei.teahub.dto.request.system.ApplicationSettingDto;
import dev.parhamziaei.teahub.entity.jpa.ApplicationSettings;
import dev.parhamziaei.teahub.entity.jpa.payment.AqayePardakhtGateway;
import dev.parhamziaei.teahub.valueobject.ProductPeriodSettings;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface AppSettingsMapStruct {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "productPeriodSettings", ignore = true)
    void toEntity(ApplicationSettingDto request, @MappingTarget ApplicationSettings entity);

}

