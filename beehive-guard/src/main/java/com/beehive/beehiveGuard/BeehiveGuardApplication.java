package com.beehive.beehiveGuard;

import com.beehive.beehiveGuard.configuration.DatabaseInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

//TODO add a proper logger
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class BeehiveGuardApplication {

    public static void main(String[] args) {
        //TODO Find a way to use spring here
        DatabaseInitializer.createDatabaseIfNotExists();
        SpringApplication.run(BeehiveGuardApplication.class, args);
    }
}
