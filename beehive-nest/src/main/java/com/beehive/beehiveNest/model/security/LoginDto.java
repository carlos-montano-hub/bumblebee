package com.beehive.beehiveNest.model.security;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    private String token;
    private String refreshToken;
}
