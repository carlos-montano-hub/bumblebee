package com.beehive.beehiveGuard.model.mappers;

import com.beehive.beehiveGuard.model.dtos.AppUserDto;
import com.beehive.beehiveGuard.model.entities.AppUser;
import com.beehive.beehiveGuard.model.forms.AppUserForm;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AppUserMapper {
    AppUserMapper INSTANCE = Mappers.getMapper(AppUserMapper.class);

    //    Entity -> Dto
    AppUserDto getDto(AppUser entity);

    //    Form -> Entity
    AppUser getEntity(AppUserForm form);
}
