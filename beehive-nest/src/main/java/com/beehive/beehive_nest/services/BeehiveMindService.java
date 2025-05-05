package com.beehive.beehive_nest.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.beehive.beehive_nest.model.internal_models.AudioRegisterForm;
import com.beehive.beehive_nest.model.internal_models.ClassificationResult;

@Service
public class BeehiveMindService {

    @Value("${beehive_mind_service_url}")
    private String beehiveMindServiceUrl;
    private String audioRegisterPath = "/api/audio";
    private final ClientService clientService;
    private final RestTemplate restTemplate;

    public BeehiveMindService(ClientService clientService) {
        this.clientService = clientService;
        this.restTemplate = clientService.getRestTemplate();
    }

    public <T> ResponseEntity<T> sendToBeehiveMindService(String path,
            HttpMethod method,
            Object requestBody,
            Class<T> responseType) {
        HttpHeaders headers = clientService.getHeaders();
        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);

        return restTemplate.exchange(
                beehiveMindServiceUrl + path,
                method,
                requestEntity,
                responseType);
    }

    public ClassificationResult registerAudio(String audioId, String label) {
        AudioRegisterForm audioRegisterForm = new AudioRegisterForm(audioId, LocalDate.now(), label);
        ResponseEntity<ClassificationResult> results = sendToBeehiveMindService(
                audioRegisterPath,
                HttpMethod.POST,
                audioRegisterForm,
                ClassificationResult.class);

        return results.getBody();
    }
}
