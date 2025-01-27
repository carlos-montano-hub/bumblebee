package com.beehive.beehiveNest.services;

import com.beehive.beehiveNest.exceptions.DependencyNotFoundException;
import com.beehive.beehiveNest.model.dtos.ApiaryDto;
import com.beehive.beehiveNest.model.entities.Apiary;
import com.beehive.beehiveNest.model.entities.AppUser;
import com.beehive.beehiveNest.model.forms.ApiaryForm;
import com.beehive.beehiveNest.model.mappers.ApiaryMapper;
import com.beehive.beehiveNest.repository.ApiaryRepository;
import com.beehive.beehiveNest.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApiaryService implements CrudService<ApiaryDto, ApiaryForm> {

    @Autowired
    private ApiaryMapper mapper;
    @Autowired
    private ApiaryRepository apiaryRepository;
    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public List<ApiaryDto> getAll() {
        return apiaryRepository.findAll().stream()
                .map(mapper::getDto)
                .toList();
    }

    @Override
    public Optional<ApiaryDto> getById(Long id) {
        return apiaryRepository.findById(id)
                .map(mapper::getDto);
    }

    @Override
    public ApiaryDto create(ApiaryForm form) {
        Apiary entity = setDependencies(form);

        Apiary savedEntity = apiaryRepository.save(entity);
        return mapper.getDto(savedEntity);
    }

    private Apiary setDependencies(ApiaryForm form) {
        Apiary entity = mapper.getEntity(form);
        AppUser user = appUserRepository.findById(form.getOwnerId())
                .orElseThrow(() -> new DependencyNotFoundException("User with ID " + form.getOwnerId() + " not found."));
        entity.setOwner(user);
        return entity;
    }

    @Override
    public Optional<ApiaryDto> update(Long id, ApiaryForm form) {
        if (!apiaryRepository.existsById(id)) {
            return Optional.empty();
        }
        Apiary entity = setDependencies(form);

        entity.setId(id);
        Apiary updatedEntity = apiaryRepository.save(entity);
        return Optional.of(mapper.getDto(updatedEntity));
    }

    @Override
    public boolean delete(Long id) {
        if (!apiaryRepository.existsById(id)) {
            return false;
        }
        apiaryRepository.deleteById(id);
        return true;
    }

    public List<ApiaryDto> findApiariesByUserId(Long userId) {
        return apiaryRepository.findByOwner_Id(userId).stream()
                .map(mapper::getDto)
                .collect(Collectors.toList());
    }
}
