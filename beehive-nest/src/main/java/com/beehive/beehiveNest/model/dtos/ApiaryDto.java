package com.beehive.beehiveNest.model.dtos;

import java.util.UUID;

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
    private UUID owner;
}
