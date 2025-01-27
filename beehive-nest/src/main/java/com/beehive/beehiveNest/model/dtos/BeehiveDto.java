package com.beehive.beehiveNest.model.dtos;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class BeehiveDto {
    private Long id;
    private String name;
    private String serial;
    private double latitude;
    private double longitude;
    private ApiaryDto apiary;
    private MeasureDto lastMeasure;
}
