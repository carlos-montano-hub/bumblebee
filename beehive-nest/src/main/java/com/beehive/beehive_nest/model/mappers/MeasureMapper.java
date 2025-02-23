package com.beehive.beehive_nest.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.beehive.beehive_nest.model.dtos.MeasureDto;
import com.beehive.beehive_nest.model.entities.Measure;
import com.beehive.beehive_nest.model.forms.MeasureForm;

@Mapper(componentModel = "spring")
public interface MeasureMapper {
    MeasureMapper INSTANCE = Mappers.getMapper(MeasureMapper.class);

    // Entity -> Dto
    MeasureDto getDto(Measure entity);

    // Form -> Entity
    @Mapping(target = "audioRecordingUrl", ignore = true)
    @Mapping(target = "beehive", ignore = true)
    @Mapping(target = "id", ignore = true)
    Measure getEntity(MeasureForm form);
}
