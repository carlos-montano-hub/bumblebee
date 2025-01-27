package com.beehive.beehiveNest.model.security;

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
