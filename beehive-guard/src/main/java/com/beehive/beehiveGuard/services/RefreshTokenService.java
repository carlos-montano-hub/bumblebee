package com.beehive.beehiveGuard.services;

import com.beehive.beehiveGuard.model.entities.AppUser;
import com.beehive.beehiveGuard.model.security.RefreshToken;
import com.beehive.beehiveGuard.repository.AppUserRepository;
import com.beehive.beehiveGuard.repository.TokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${app.jwt.secret}")
    private String secretKey;
    
    @Value("${app.jwt.refreshTokenExpiryMs}")
    private Long refreshTokenExpiryMs;
    
    @Autowired
    private TokenRepository tokenRepository;

    public RefreshToken createRefreshToken(AppUser user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpiryMs));
        return tokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            tokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired");
        }
        return Optional.of(token);
    }

    public String generateToken(String username, long expirationDays) {
        // Generate JWT token with user info and expiration
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationDays))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public void deleteByUser(AppUser user) {
        tokenRepository.deleteByUser(user);
    }
}
