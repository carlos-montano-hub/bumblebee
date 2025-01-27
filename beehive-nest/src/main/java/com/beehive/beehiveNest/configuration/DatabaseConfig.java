package com.beehive.beehiveNest.configuration;

import com.beehive.beehiveNest.services.TestDataService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {

    @Autowired
    private TestDataService testDataService;

    @PostConstruct
    public void runTestData() {
        testDataService.insertTestData();
    }
}
