package com.beehive.beehiveNest.services;

import com.beehive.beehiveNest.model.dtos.AppUserDto;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {

    private ApiExchangeService apiExchangeService;

    public AppUserService(ApiExchangeService apiExchangeService) {
        this.apiExchangeService = apiExchangeService;
    }

    public AppUserDto findByEmail(String email) {
        ResponseEntity<AppUserDto> response = (ResponseEntity<AppUserDto>) apiExchangeService.sendToGuardService(
                "/auth/profile/" + email,
                HttpMethod.GET,
                null,
                AppUserDto.class);
        return response.getBody();
    }
}
