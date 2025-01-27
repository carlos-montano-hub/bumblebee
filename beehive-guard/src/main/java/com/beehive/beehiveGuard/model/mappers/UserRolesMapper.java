package com.beehive.beehiveGuard.model.mappers;

import com.beehive.beehiveGuard.model.dtos.UserRoleDto;
import com.beehive.beehiveGuard.model.entities.UserRole;
import com.beehive.beehiveGuard.model.forms.UserRoleForm;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserRolesMapper {
    UserRolesMapper INSTANCE = Mappers.getMapper(UserRolesMapper.class);

    //    Entity -> Dto
    UserRoleDto getDto(UserRole entity);

    //    Form -> Entity
    UserRole getEntity(UserRoleForm form);
}
