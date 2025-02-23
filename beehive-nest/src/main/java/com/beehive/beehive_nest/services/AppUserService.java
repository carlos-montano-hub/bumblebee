package com.beehive.beehive_nest.services;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.beehive.beehive_nest.model.dtos.AppUserDto;

@Service
public class AppUserService {

    private GuardServiceClient guardServiceClient;

    public AppUserService(GuardServiceClient guardServiceClient) {
        this.guardServiceClient = guardServiceClient;
    }

    public AppUserDto findByEmail(String email) {
        ResponseEntity<AppUserDto> response = guardServiceClient.sendToGuardService(
                "/auth/profile/" + email,
                HttpMethod.GET,
                null,
                AppUserDto.class);
        return response.getBody();
    }
}
