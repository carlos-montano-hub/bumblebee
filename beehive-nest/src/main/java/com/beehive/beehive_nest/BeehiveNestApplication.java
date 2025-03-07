package com.beehive.beehive_nest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

import com.beehive.beehive_nest.configuration.DatabaseInitializer;

//TODO add a proper logger
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class BeehiveNestApplication {

    public static void main(String[] args) {
        // TODO Find a way to use spring here
        DatabaseInitializer.createDatabaseIfNotExists();
        SpringApplication.run(BeehiveNestApplication.class, args);
    }
}
