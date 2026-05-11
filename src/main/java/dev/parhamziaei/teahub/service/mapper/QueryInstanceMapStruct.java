package dev.parhamziaei.teahub.service.mapper;

import dev.parhamziaei.teahub.dto.request.teaspeak.admin.QueryInstanceEditRequest;
import dev.parhamziaei.teahub.dto.request.teaspeak.admin.QueryInstanceInitRequest;
import dev.parhamziaei.teahub.entity.jpa.teaspeak.QueryInstance;
import dev.parhamziaei.teahub.integration.teaspeak_query.model.ServerQueryCredentials;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface QueryInstanceMapStruct {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntity(QueryInstanceEditRequest request, @MappingTarget QueryInstance entity);

    default <T extends QueryInstanceInitRequest> void credentialsMapping(T request, QueryInstance entity) {
        ServerQueryCredentials credentials = new ServerQueryCredentials(
                request.getQueryIpAddress() != null ? request.getQueryIpAddress() : entity.getCredentials().ip(),
                request.getQueryPort() != null ? request.getQueryPort() : entity.getCredentials().port(),
                request.getQueryUsername() != null ? request.getQueryUsername() : entity.getCredentials().username(),
                request.getQueryPassword() != null ? request.getQueryPassword() : entity.getCredentials().password()
        );
        entity.setCredentials(credentials);
    }

}
