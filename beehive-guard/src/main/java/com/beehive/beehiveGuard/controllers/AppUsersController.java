package com.beehive.beehiveGuard.controllers;

import com.beehive.beehiveGuard.model.dtos.AppUserDto;
import com.beehive.beehiveGuard.model.forms.AppUserForm;
import com.beehive.beehiveGuard.services.AppUserService;
import com.beehive.beehiveGuard.services.TokenService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appUsers")
public class AppUsersController {

    private AppUserService service;

    public AppUsersController(AppUserService service, TokenService tokenService) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AppUserDto>> getAllEntities() {
        List<AppUserDto> entities = service.getAll();
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUserDto> getEntityById(@PathVariable UUID id) {
        Optional<AppUserDto> entity = service.getById(id);
        return entity.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AppUserDto> createEntity(@RequestBody AppUserForm entityForm) {
        AppUserDto createdEntity = service.create(entityForm);
        return ResponseEntity.status(200).body(createdEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUserDto> updateEntity(@PathVariable UUID id, @RequestBody AppUserForm entityForm) {
        Optional<AppUserDto> updatedEntity = service.update(id, entityForm);
        return updatedEntity.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntity(@PathVariable UUID id) {
        if (service.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
