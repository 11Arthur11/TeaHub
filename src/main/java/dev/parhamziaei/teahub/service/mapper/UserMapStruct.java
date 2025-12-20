package dev.parhamziaei.teahub.service.mapper;

import dev.parhamziaei.teahub.dto.response.user.admin.UserEditAdminRequest;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapStruct {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(UserEditAdminRequest request, @MappingTarget User user);

}

