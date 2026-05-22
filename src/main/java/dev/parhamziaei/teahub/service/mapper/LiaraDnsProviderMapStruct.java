package dev.parhamziaei.teahub.service.mapper;

import dev.parhamziaei.teahub.dto.request.dns.admin.LiaraDnsProviderPersistRequest;
import dev.parhamziaei.teahub.entity.jpa.dns.LiaraDnsProvider;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LiaraDnsProviderMapStruct {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntity(LiaraDnsProviderPersistRequest registerRequest, @MappingTarget LiaraDnsProvider dnsProvider);

}
