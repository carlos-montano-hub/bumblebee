package com.beehive.beehiveNest.model.mappers;

import com.beehive.beehiveNest.model.dtos.BeehiveDto;
import com.beehive.beehiveNest.model.entities.Beehive;
import com.beehive.beehiveNest.model.forms.BeehiveForm;
import com.beehive.beehiveNest.model.forms.SelfRegisterBeehiveForm;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BeehiveMapper {
    BeehiveMapper INSTANCE = Mappers.getMapper(BeehiveMapper.class);

    //    Entity -> Dto
    BeehiveDto getDto(Beehive entity);

    //    Form -> Entity
    Beehive getEntity(BeehiveForm form);

    Beehive getEntity(SelfRegisterBeehiveForm form);
}
