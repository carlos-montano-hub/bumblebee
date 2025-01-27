package com.beehive.beehiveNest.model.mappers;

import com.beehive.beehiveNest.model.dtos.MeasureDto;
import com.beehive.beehiveNest.model.entities.Measure;
import com.beehive.beehiveNest.model.forms.MeasureForm;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MeasureMapper {
    MeasureMapper INSTANCE = Mappers.getMapper(MeasureMapper.class);

    //    Entity -> Dto
    MeasureDto getDto(Measure entity);

    //    Form -> Entity
    Measure getEntity(MeasureForm form);
}
