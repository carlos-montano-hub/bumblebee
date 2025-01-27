package com.beehive.beehiveNest.repository;

import com.beehive.beehiveNest.model.entities.AppUser;
import com.beehive.beehiveNest.model.security.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(AppUser appUser);

    void deleteByUser(AppUser user);
}
