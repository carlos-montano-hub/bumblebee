package com.beehive.beehiveGuard.controllers;

import com.beehive.beehiveGuard.model.dtos.AppUserDto;
import com.beehive.beehiveGuard.model.entities.AppUser;
import com.beehive.beehiveGuard.model.forms.AppUserForm;
import com.beehive.beehiveGuard.model.forms.RegisterForm;
import com.beehive.beehiveGuard.model.security.LoginDto;
import com.beehive.beehiveGuard.model.security.LoginForm;
import com.beehive.beehiveGuard.model.security.RefreshTokenForm;
import com.beehive.beehiveGuard.services.AppUserService;
import com.beehive.beehiveGuard.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AppUserService appUserService;
    private final TokenService tokenService;

    @Autowired
    public AuthController(AppUserService appUserService, TokenService tokenService) {
        this.appUserService = appUserService;
        this.tokenService = tokenService;
    }

    @GetMapping("/profile")
    public ResponseEntity<AppUserDto> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        AppUserDto profile = appUserService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDto> login(@RequestBody LoginForm request) {
        AppUser user = appUserService.authenticate(request.getEmail(), request.getPassword());
        String accessToken = tokenService.generateToken(user);
        String refreshToken = tokenService.fetchRefreshToken(user);  // Generate refresh token


        return ResponseEntity.ok(new LoginDto(accessToken, refreshToken));  // Send both tokens
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody RefreshTokenForm refreshTokenForm) {
        String refreshToken = refreshTokenForm.getRefreshToken();

        if (tokenService.validateToken(refreshToken)) {
            String newAccessToken = tokenService.regenerateAccessToken(refreshToken);
            return ResponseEntity.ok(new LoginDto(newAccessToken, refreshToken));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid refresh token");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AppUserDto> registerUser(@RequestBody RegisterForm form) {
        AppUserDto createdEntity = appUserService.create(new AppUserForm(
                form.getName(), form.getPhoneNumber(), form.getEmailAddress(), 2L, form.getPassword())
        );
        return ResponseEntity.status(200).body(createdEntity);
    }


}
