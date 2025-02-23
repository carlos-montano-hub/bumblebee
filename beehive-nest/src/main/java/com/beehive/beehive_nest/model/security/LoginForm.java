package com.beehive.beehive_nest.model.security;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm {
    private String email;
    private String password;
}
