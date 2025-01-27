package com.beehive.beehiveGuard.model.security;

import com.beehive.beehiveGuard.model.entities.AppUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne
    @JoinColumn(name = "app_user_id", nullable = false)
    private AppUser user;

    @Column(nullable = false)
    private Instant expiryDate;
}
