package com.beehive.beehive_nest.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.beehive.beehive_nest.services.TestDataService;

@Configuration
public class DatabaseConfig {

    @Autowired
    private TestDataService testDataService;

    @PostConstruct
    public void runTestData() {
        testDataService.insertTestData();
    }
}
