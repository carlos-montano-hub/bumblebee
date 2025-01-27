package com.beehive.beehiveGuard.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phoneNumber;
    @Column(nullable = false, unique = true)
    private String emailAddress;
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    @JsonBackReference
    private UserRole role;
    @Column(nullable = false)
    private String password;
}

