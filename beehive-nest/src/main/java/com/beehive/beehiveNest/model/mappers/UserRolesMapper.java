package com.beehive.beehiveNest.model.mappers;

import com.beehive.beehiveNest.model.dtos.UserRoleDto;
import com.beehive.beehiveNest.model.entities.UserRole;
import com.beehive.beehiveNest.model.forms.UserRoleForm;
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
