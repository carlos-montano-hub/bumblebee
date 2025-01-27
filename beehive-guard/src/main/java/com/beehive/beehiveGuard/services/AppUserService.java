package com.beehive.beehiveGuard.services;

import com.beehive.beehiveGuard.exceptions.DependencyNotFoundException;
import com.beehive.beehiveGuard.model.dtos.AppUserDto;
import com.beehive.beehiveGuard.model.entities.AppUser;
import com.beehive.beehiveGuard.model.entities.UserRole;
import com.beehive.beehiveGuard.model.forms.AppUserForm;
import com.beehive.beehiveGuard.model.mappers.AppUserMapper;
import com.beehive.beehiveGuard.repository.AppUserRepository;
import com.beehive.beehiveGuard.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserService implements CrudService<AppUserDto, AppUserForm> {

    @Autowired
    private AppUserMapper appUserMapper;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public AppUser authenticate(String email, String rawPassword) {
        AppUser user = appUserRepository.findByEmailAddress(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return user;
    }

    public AppUserDto findByEmail(String email) {
        return appUserRepository.findByEmailAddress(email)
                .map(appUserMapper::getDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    @Override
    public List<AppUserDto> getAll() {
        return appUserRepository.findAll().stream()
                .map(appUserMapper::getDto)
                .toList();
    }

    @Override
    public Optional<AppUserDto> getById(Long id) {
        return appUserRepository.findById(id)
                .map(appUserMapper::getDto);
    }

    @Override
    public AppUserDto create(AppUserForm form) {
        AppUser entity = appUserMapper.getEntity(form);
        entity.setPassword(passwordEncoder.encode(form.getPassword()));

        UserRole role = userRoleRepository.findById(form.getRoleId())
                .orElseThrow(() -> new DependencyNotFoundException("User Role with ID " + form.getRoleId() + " not found."));
        entity.setRole(role);

        if (appUserRepository.findByEmailAddress(form.getEmailAddress()).isPresent()) {
            throw new RuntimeException("User email repeated");
        }

        AppUser savedEntity = appUserRepository.save(entity);
        return appUserMapper.getDto(savedEntity);
    }

    @Override
    public Optional<AppUserDto> update(Long id, AppUserForm form) {
        if (!appUserRepository.existsById(id)) {
            return Optional.empty();
        }
        AppUser entity = appUserMapper.getEntity(form);
        entity.setPassword(passwordEncoder.encode(form.getPassword()));

        UserRole role = userRoleRepository.findById(form.getRoleId())
                .orElseThrow(() -> new DependencyNotFoundException("User Role with ID " + form.getRoleId() + " not found."));
        entity.setRole(role);
        entity.setId(id);
        AppUser updatedEntity = appUserRepository.save(entity);
        return Optional.of(appUserMapper.getDto(updatedEntity));
    }

    @Override
    public boolean delete(Long id) {
        if (!appUserRepository.existsById(id)) {
            return false;
        }
        appUserRepository.deleteById(id);
        return true;
    }
}
