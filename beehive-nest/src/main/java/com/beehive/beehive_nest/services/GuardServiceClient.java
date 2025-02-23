package com.beehive.beehive_nest.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.beehive.beehive_nest.model.dtos.AppUserDto;

@Service
public class GuardServiceClient {

    @Value("${guard_service_url}")
    private String guardServiceUrl;
    private final ClientService clientService;
    private final RestTemplate restTemplate;

    public GuardServiceClient(ClientService clientService) {
        this.clientService = clientService;
        this.restTemplate = clientService.getRestTemplate();
    }

    public <T> ResponseEntity<T> sendToGuardService(String path,
            HttpMethod method,
            Object requestBody,
            Class<T> responseType) {
        HttpHeaders headers = clientService.getHeaders();
        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);

        return restTemplate.exchange(
                guardServiceUrl + path,
                method,
                requestEntity,
                responseType);
    }

    public AppUserDto getFromUserService(UUID id) {
        String path = "/api/appUsers/" + id.toString();
        ResponseEntity<AppUserDto> response = sendToGuardService(
                path,
                HttpMethod.GET,
                null,
                AppUserDto.class);
        return response.getBody();
    }
}
