package com.beehive.beehive_nest.services;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;

public class ClientService {
    @Getter
    private final RestTemplate restTemplate = new RestTemplate();
    private final String API_KEY = System.getenv("API_KEY");

    HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-Key", API_KEY);
        return headers;
    }
}
