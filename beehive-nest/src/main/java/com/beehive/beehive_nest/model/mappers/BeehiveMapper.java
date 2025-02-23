package com.beehive.beehive_nest.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.beehive.beehive_nest.model.dtos.BeehiveDto;
import com.beehive.beehive_nest.model.entities.Beehive;
import com.beehive.beehive_nest.model.forms.BeehiveForm;
import com.beehive.beehive_nest.model.forms.SelfRegisterBeehiveForm;

@Mapper(componentModel = "spring")
public interface BeehiveMapper {
    BeehiveMapper INSTANCE = Mappers.getMapper(BeehiveMapper.class);

    // Entity -> Dto
    BeehiveDto getDto(Beehive entity);

    // Form -> Entity
    Beehive getEntity(BeehiveForm form);

    Beehive getEntity(SelfRegisterBeehiveForm form);
}
