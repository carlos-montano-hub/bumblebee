package com.beehive.beehive_nest.model.dtos;

import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class AppUserDto {
    private UUID id;
    private String name;
    private String phoneNumber;
    private String emailAddress;
    private UserRoleDto role;
}
