package dev.parhamziaei.teahub.service.mapper;

import dev.parhamziaei.teahub.dto.request.payment.admin.AqayePardakhtPersistRequest;
import dev.parhamziaei.teahub.entity.jpa.payment.AqayePardakhtGateway;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AqayePardakhtGatewayMapStruct {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AqayePardakhtGateway toEntity(AqayePardakhtPersistRequest request, @MappingTarget AqayePardakhtGateway gateway);

}
