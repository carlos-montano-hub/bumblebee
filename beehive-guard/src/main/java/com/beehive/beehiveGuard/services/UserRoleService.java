package com.beehive.beehiveGuard.services;

import com.beehive.beehiveGuard.model.dtos.UserRoleDto;
import com.beehive.beehiveGuard.model.entities.UserRole;
import com.beehive.beehiveGuard.model.forms.UserRoleForm;
import com.beehive.beehiveGuard.model.mappers.UserRolesMapper;
import com.beehive.beehiveGuard.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserRoleService implements CrudService<UserRoleDto, UserRoleForm> {

    @Autowired
    private UserRolesMapper userRolesMapper;
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public List<UserRoleDto> getAll() {
        return userRoleRepository.findAll().stream()
                .map(userRolesMapper::getDto)
                .toList();
    }

    @Override
    public Optional<UserRoleDto> getById(Long id) {
        return userRoleRepository.findById(id)
                .map(userRolesMapper::getDto);
    }

    @Override
    public UserRoleDto create(UserRoleForm form) {
        UserRole entity = userRolesMapper.getEntity(form);
        UserRole savedEntity = userRoleRepository.save(entity);
        return userRolesMapper.getDto(savedEntity);
    }

    @Override
    public Optional<UserRoleDto> update(Long id, UserRoleForm form) {
        if (!userRoleRepository.existsById(id)) {
            return Optional.empty();
        }
        UserRole entity = userRolesMapper.getEntity(form);
        entity.setId(id);
        UserRole updatedEntity = userRoleRepository.save(entity);
        return Optional.of(userRolesMapper.getDto(updatedEntity));
    }

    @Override
    public boolean delete(Long id) {
        if (!userRoleRepository.existsById(id)) {
            return false;
        }
        userRoleRepository.deleteById(id);
        return true;
    }
}
