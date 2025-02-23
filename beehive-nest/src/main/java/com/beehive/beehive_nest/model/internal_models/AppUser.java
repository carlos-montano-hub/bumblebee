package com.beehive.beehive_nest.model.internal_models;

import java.util.UUID;

import com.beehive.beehive_nest.model.dtos.UserRoleDto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
