package com.beehive.beehive_nest.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.beehive.beehive_nest.model.dtos.ApiaryDto;
import com.beehive.beehive_nest.model.entities.Apiary;
import com.beehive.beehive_nest.model.forms.ApiaryForm;

@Mapper(componentModel = "spring")
public interface ApiaryMapper {
    ApiaryMapper INSTANCE = Mappers.getMapper(ApiaryMapper.class);

    // Entity -> Dto
    ApiaryDto getDto(Apiary entity);

    // Form -> Entity
    Apiary getEntity(ApiaryForm form);
}
