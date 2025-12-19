package dev.parhamziaei.teahub.service.mapper;

import dev.parhamziaei.teahub.dto.response.user.admin.UserEditAdminRequest;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

//@Component("userMapper")
//@Mapper(componentModel = "spring")
public interface UserMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(UserEditAdminRequest request, @MappingTarget User user);

}

