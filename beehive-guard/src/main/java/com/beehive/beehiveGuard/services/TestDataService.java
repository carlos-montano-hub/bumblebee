package com.beehive.beehiveGuard.services;

import com.beehive.beehiveGuard.configuration.DatabaseInitializer;
import com.beehive.beehiveGuard.model.entities.*;
import com.beehive.beehiveGuard.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TestDataService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Transactional
    public void insertTestData() {
        if (!DatabaseInitializer.isInitialized()) {
            return;
        }
        UserRole adminRole = new UserRole(1L, "Admin");
        UserRole beekeeperRole = new UserRole(2L, "Beekeeper");
        userRoleRepository.save(adminRole);
        userRoleRepository.save(beekeeperRole);

        AppUser adminUser = new AppUser(1L, "Admin User", "1234567890", "admin@example.com", adminRole, "$2a$10$eRwbLEwfv.Lb0QA8jmqMSeviFaNEt5mT/Sa5SydsATx47rM5c7KRi");
        AppUser johnDoe = new AppUser(2L, "John Doe", "9876543210", "beekeeper1@example.com", beekeeperRole, "$2a$10$eRwbLEwfv.Lb0QA8jmqMSeviFaNEt5mT/Sa5SydsATx47rM5c7KRi");
        AppUser janeSmith = new AppUser(3L, "Jane Smith", "1122334455", "beekeeper2@example.com", beekeeperRole, "$2a$10$eRwbLEwfv.Lb0QA8jmqMSeviFaNEt5mT/Sa5SydsATx47rM5c7KRi");
        AppUser carlos = new AppUser(4L, "Carlos Montaño", "6621438271", "montanoc70@gmail.com", adminRole, "$2a$10$eRwbLEwfv.Lb0QA8jmqMSeviFaNEt5mT/Sa5SydsATx47rM5c7KRi");
        appUserRepository.save(adminUser);
        appUserRepository.save(johnDoe);
        appUserRepository.save(janeSmith);
        appUserRepository.save(carlos);

    }
}
