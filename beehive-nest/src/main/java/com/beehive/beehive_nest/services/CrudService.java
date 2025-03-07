package com.beehive.beehive_nest.services;

import java.util.List;
import java.util.Optional;

public interface CrudService<EntityDto, EntityForm> {
    List<EntityDto> getAll();

    Optional<EntityDto> getById(Long id);

    EntityDto create(EntityForm form);

    Optional<EntityDto> update(Long id, EntityForm form);

    boolean delete(Long id);
}
