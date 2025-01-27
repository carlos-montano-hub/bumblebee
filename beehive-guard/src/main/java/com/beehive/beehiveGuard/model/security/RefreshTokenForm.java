package com.beehive.beehiveGuard.model.security;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenForm {
    private String refreshToken;
}
