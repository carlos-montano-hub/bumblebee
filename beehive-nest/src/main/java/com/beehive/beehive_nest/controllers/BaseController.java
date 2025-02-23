package com.beehive.beehive_nest.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.beehive.beehive_nest.services.CrudService;

import java.util.List;
import java.util.Optional;

public abstract class BaseController<EntityDto, EntityForm> {

    private final CrudService<EntityDto, EntityForm> service;

    public BaseController(CrudService<EntityDto, EntityForm> service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<EntityDto>> getAllEntities() {
        List<EntityDto> entities = service.getAll();
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityDto> getEntityById(@PathVariable Long id) {
        Optional<EntityDto> entity = service.getById(id);
        return entity.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EntityDto> createEntity(@RequestBody EntityForm entityForm) {
        EntityDto createdEntity = service.create(entityForm);
        return ResponseEntity.status(200).body(createdEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityDto> updateEntity(@PathVariable Long id, @RequestBody EntityForm entityForm) {
        Optional<EntityDto> updatedEntity = service.update(id, entityForm);
        return updatedEntity.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntity(@PathVariable Long id) {
        if (service.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
