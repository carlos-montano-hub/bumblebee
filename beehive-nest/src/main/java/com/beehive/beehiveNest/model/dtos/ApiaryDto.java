package com.beehive.beehiveNest.model.dtos;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class ApiaryDto {
    private Long id;
    private String name;
    private AppUserDto owner;
}
