package com.beehive.beehive_nest.controllers;

import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.beehive.beehive_nest.model.dtos.AppUserDto;
import com.beehive.beehive_nest.model.forms.RegisterForm;
import com.beehive.beehive_nest.model.security.LoginDto;
import com.beehive.beehive_nest.model.security.LoginForm;
import com.beehive.beehive_nest.model.security.RefreshTokenForm;
import com.beehive.beehive_nest.services.GuardServiceClient;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final GuardServiceClient guardServiceClient;

    public AuthController(GuardServiceClient guardServiceClient) {
        this.guardServiceClient = guardServiceClient;
    }

    @GetMapping("/profile")
    public ResponseEntity<AppUserDto> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        ResponseEntity<AppUserDto> response = guardServiceClient.sendToGuardService(
                "/auth/profile/" + userDetails.getUsername(),
                HttpMethod.GET,
                null,
                AppUserDto.class);

        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDto> login(@RequestBody LoginForm request) {
        ResponseEntity<LoginDto> response = guardServiceClient.sendToGuardService(
                "/auth/login",
                HttpMethod.POST,
                request,
                LoginDto.class);

        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginDto> refreshAccessToken(@RequestBody RefreshTokenForm refreshTokenForm) {
        ResponseEntity<LoginDto> response = guardServiceClient.sendToGuardService(
                "/auth/refresh",
                HttpMethod.POST,
                refreshTokenForm,
                LoginDto.class);

        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/register")
    public ResponseEntity<AppUserDto> registerUser(@RequestBody RegisterForm form) {
        ResponseEntity<AppUserDto> response = guardServiceClient.sendToGuardService(
                "/auth/register",
                HttpMethod.POST,
                form,
                AppUserDto.class);

        return ResponseEntity.ok(response.getBody());
    }
}
