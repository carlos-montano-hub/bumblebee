package com.beehive.beehiveNest.controllers;

import com.beehive.beehiveNest.model.dtos.AppUserDto;
import com.beehive.beehiveNest.model.forms.RegisterForm;
import com.beehive.beehiveNest.model.security.LoginDto;
import com.beehive.beehiveNest.model.security.LoginForm;
import com.beehive.beehiveNest.model.security.RefreshTokenForm;
import com.beehive.beehiveNest.services.ApiExchangeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private ApiExchangeService apiExchangeService;

    @GetMapping("/profile")
    public ResponseEntity<AppUserDto> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        ResponseEntity<AppUserDto> response = (ResponseEntity<AppUserDto>) apiExchangeService.sendToGuardService(
                "/auth/profile/" + userDetails.getUsername(),
                HttpMethod.GET,
                null,
                AppUserDto.class);

        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDto> login(@RequestBody LoginForm request) {
        ResponseEntity<LoginDto> response = (ResponseEntity<LoginDto>) apiExchangeService.sendToGuardService(
                "/auth/login",
                HttpMethod.POST,
                request,
                LoginDto.class);

        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody RefreshTokenForm refreshTokenForm) {
        ResponseEntity<LoginDto> response = (ResponseEntity<LoginDto>) apiExchangeService.sendToGuardService(
                "/auth/refresh",
                HttpMethod.POST,
                refreshTokenForm,
                LoginDto.class);

        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/register")
    public ResponseEntity<AppUserDto> registerUser(@RequestBody RegisterForm form) {
        ResponseEntity<AppUserDto> response = (ResponseEntity<AppUserDto>) apiExchangeService.sendToGuardService(
                "/auth/register",
                HttpMethod.POST,
                form,
                AppUserDto.class);

        return ResponseEntity.ok(response.getBody());
    }
}
