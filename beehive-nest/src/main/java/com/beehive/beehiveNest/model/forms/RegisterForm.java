package com.beehive.beehiveNest.model.forms;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RegisterForm {
    private String name;
    private String phoneNumber;
    private String emailAddress;
    private String password;
}

