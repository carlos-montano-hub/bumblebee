package com.beehive.beehiveNest.model.mappers;

import com.beehive.beehiveNest.model.dtos.ApiaryDto;
import com.beehive.beehiveNest.model.entities.Apiary;
import com.beehive.beehiveNest.model.forms.ApiaryForm;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ApiaryMapper {
    ApiaryMapper INSTANCE = Mappers.getMapper(ApiaryMapper.class);

    //    Entity -> Dto
    ApiaryDto getDto(Apiary entity);

    //    Form -> Entity
    Apiary getEntity(ApiaryForm form);
}
