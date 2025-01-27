package com.beehive.beehiveNest.model.forms;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AppUserForm {
    private String name;
    private String phoneNumber;
    private String emailAddress;
    private long roleId;
    private String password;
}

