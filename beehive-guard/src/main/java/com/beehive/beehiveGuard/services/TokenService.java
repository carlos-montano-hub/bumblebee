package com.beehive.beehiveGuard.services;

import com.beehive.beehiveGuard.model.entities.AppUser;
import com.beehive.beehiveGuard.model.security.RefreshToken;
import com.beehive.beehiveGuard.repository.AppUserRepository;
import com.beehive.beehiveGuard.repository.TokenRepository;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
public class TokenService {
    private SecretKey key;

    private final int expirationMs = 86400000; // 1 day for refresh tokens

    private final TokenRepository tokenRepository;
    private final AppUserRepository appUserRepository;

    public TokenService(TokenRepository tokenRepository, AppUserRepository appUserRepository,
            @Value("${app.jwt.secret}") String secretKey) {
        this.key = new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS256.getJcaName());
        this.tokenRepository = tokenRepository;
        this.appUserRepository = appUserRepository;
    }

    public String generateToken(AppUser user) {
        return generateToken(user, expirationMs);
    }

    public String generateToken(AppUser user, long expirationMs) {
        String email = user.getEmailAddress();
        long role = user.getRole().getId();
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }

    public String fetchRefreshToken(AppUser user) {
        Optional<RefreshToken> refreshTokenOptional = tokenRepository.findByUser(user);
        if (refreshTokenOptional.isPresent()) {
            RefreshToken oldRefreshToken = refreshTokenOptional.get();
            if (validateToken(oldRefreshToken.getToken())) {
                return oldRefreshToken.getToken();
            } else {
                tokenRepository.delete(oldRefreshToken);
            }
        }

        RefreshToken refreshToken = generateRefreshToken(user);
        return refreshToken.getToken();
    }

    public String regenerateAccessToken(String oldToken) {
        Optional<AppUser> userOptional = appUserRepository.findByEmailAddress(getEmailFromToken(oldToken));
        if (userOptional.isPresent()) {
            return generateToken(userOptional.get());
        }

        throw new RuntimeException("User not found");
    }

    private RefreshToken generateRefreshToken(AppUser user) {
        long expirationDays = 3;
        long expirationTime = expirationDays * expirationMs;
        String refreshTokenString = generateToken(user, expirationTime);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(refreshTokenString);
        refreshToken.setExpiryDate(Instant.now().plusMillis(expirationTime));
        return refreshToken;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            Date expiration = claims.getBody().getExpiration();
            return !expiration.before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    public long getRoleFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("role", Long.class);
    }
}
