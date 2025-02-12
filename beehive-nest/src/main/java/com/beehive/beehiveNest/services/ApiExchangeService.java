package com.beehive.beehiveNest.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.beehive.beehiveNest.model.dtos.AppUserDto;

@Service
public class ApiExchangeService {

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${guard_service_url}")
    private String guardServiceUrl;
    private final String API_KEY = System.getenv("API_KEY");

    public <T> ResponseEntity<T> sendToGuardService(String path,
            HttpMethod method,
            Object requestBody,
            Class<T> responseType) {
        HttpHeaders headers = getHeaders();
        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);

        return restTemplate.exchange(
                guardServiceUrl + path,
                method,
                requestEntity,
                responseType);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-Key", API_KEY);
        return headers;
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
