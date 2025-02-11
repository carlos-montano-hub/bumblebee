package com.beehive.beehiveNest.model.InternalModels;

import java.util.UUID;

import com.beehive.beehiveNest.model.dtos.UserRoleDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class AppUser {
    private UUID id;
    private String name;
    private String phoneNumber;
    private String emailAddress;
    private UserRoleDto role;
    private String password;
}
