package com.beehive.beehive_nest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beehive.beehive_nest.exceptions.DependencyNotFoundException;
import com.beehive.beehive_nest.model.dtos.BeehiveDto;
import com.beehive.beehive_nest.model.dtos.MeasureDto;
import com.beehive.beehive_nest.model.entities.Apiary;
import com.beehive.beehive_nest.model.entities.Beehive;
import com.beehive.beehive_nest.model.entities.Measure;
import com.beehive.beehive_nest.model.forms.BeehiveForm;
import com.beehive.beehive_nest.model.forms.CheckForm;
import com.beehive.beehive_nest.model.forms.RegisterBeehiveForm;
import com.beehive.beehive_nest.model.forms.SelfRegisterBeehiveForm;
import com.beehive.beehive_nest.model.mappers.BeehiveMapper;
import com.beehive.beehive_nest.model.mappers.MeasureMapper;
import com.beehive.beehive_nest.repository.ApiaryRepository;
import com.beehive.beehive_nest.repository.BeehiveRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BeehiveService implements CrudService<BeehiveDto, BeehiveForm> {

    @Autowired
    private BeehiveMapper mapper;
    @Autowired
    private BeehiveRepository beehiveRepository;
    @Autowired
    private ApiaryRepository apiaryRepository;
    @Autowired
    private MeasureMapper measureMapper;

    @Override
    public List<BeehiveDto> getAll() {
        return beehiveRepository.findAll().stream()
                .map(mapper::getDto)
                .toList();
    }

    @Override
    public Optional<BeehiveDto> getById(Long id) {
        return beehiveRepository.findById(id)
                .map(beehive -> {
                    BeehiveDto beehiveDto = mapper.getDto(beehive);
                    getLastMeasure(beehive).ifPresent(beehiveDto::setLastMeasure);
                    return beehiveDto;
                });
    }

    @Override
    public BeehiveDto create(BeehiveForm form) {
        Beehive entity = setDependencies(form);

        Beehive savedEntity = beehiveRepository.save(entity);
        return mapper.getDto(savedEntity);
    }

    private Beehive setDependencies(BeehiveForm form) {
        Beehive entity = mapper.getEntity(form);
        Apiary apiary = apiaryRepository.findById(form.getApiaryId())
                .orElseThrow(
                        () -> new DependencyNotFoundException("Apiary with ID " + form.getApiaryId() + " not found."));
        entity.setApiary(apiary);
        return entity;
    }

    @Override
    public Optional<BeehiveDto> update(Long id, BeehiveForm form) {
        if (!beehiveRepository.existsById(id)) {
            return Optional.empty();
        }
        Beehive entity = setDependencies(form);

        entity.setId(id);
        Beehive updatedEntity = beehiveRepository.save(entity);
        return Optional.of(mapper.getDto(updatedEntity));
    }

    @Override
    public boolean delete(Long id) {
        if (!beehiveRepository.existsById(id)) {
            return false;
        }
        beehiveRepository.deleteById(id);
        return true;
    }

    private Optional<MeasureDto> getLastMeasure(Beehive beehive) {
        if (beehive.getMeasures() != null && !beehive.getMeasures().isEmpty()) {
            return beehive.getMeasures().stream().max(Comparator.comparing(Measure::getTime))
                    .map(measureMapper::getDto);
        }
        return Optional.empty();
    }

    public List<BeehiveDto> findBeehivesByApiaryId(Long apiaryId) {
        return beehiveRepository.findByApiary_Id(apiaryId)
                .stream()
                .map(beehive -> {
                    BeehiveDto beehiveDto = mapper.getDto(beehive);
                    getLastMeasure(beehive).ifPresent(beehiveDto::setLastMeasure);
                    return beehiveDto;
                })
                .collect(Collectors.toList());
    }

    public BeehiveDto register(RegisterBeehiveForm form) {
        Optional<Beehive> optionalBeehive = beehiveRepository.findBySerial(form.getSerial());
        if (optionalBeehive.isEmpty()) {
            throw new DependencyNotFoundException("Beehive with serial " + form.getSerial() + " not found.");
        }
        Beehive beehive = optionalBeehive.get();

        Apiary apiary = apiaryRepository.findById(form.getApiaryId())
                .orElseThrow(
                        () -> new DependencyNotFoundException("Apiary with ID " + form.getApiaryId() + " not found."));
        beehive.setApiary(apiary);
        beehive.setName("Hive Test");
        Beehive savedEntity = beehiveRepository.save(beehive);
        return mapper.getDto(savedEntity);
    }

    public void selfRegister(SelfRegisterBeehiveForm form) {
        Beehive entity = mapper.getEntity(form);
        beehiveRepository.save(entity);
    }

    public boolean checkBeehiveSerial(CheckForm checkForm) {
        Optional<Beehive> optionalBeehive = beehiveRepository.findBySerial(checkForm.getSerial());
        return optionalBeehive.isPresent();
    }

}
