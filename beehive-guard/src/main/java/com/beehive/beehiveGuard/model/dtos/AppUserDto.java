package com.beehive.beehiveGuard.model.dtos;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class AppUserDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private String emailAddress;
    private UserRoleDto role;
}

