package com.beehive.beehive_nest.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beehive.beehive_nest.exceptions.DependencyNotFoundException;
import com.beehive.beehive_nest.model.dtos.MeasureDto;
import com.beehive.beehive_nest.model.entities.Beehive;
import com.beehive.beehive_nest.model.entities.Measure;
import com.beehive.beehive_nest.model.forms.MeasureForm;
import com.beehive.beehive_nest.model.internal_models.ClassificationResult;
import com.beehive.beehive_nest.model.mappers.MeasureMapper;
import com.beehive.beehive_nest.repository.BeehiveRepository;
import com.beehive.beehive_nest.repository.MeasureRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class MeasureService implements CrudService<MeasureDto, MeasureForm> {

    private final MeasureMapper mapper;
    private final MeasureRepository measureRepository;
    private final BeehiveRepository beehiveRepository;
    private final AudioService audioService;
    private final BeehiveMindService beehiveMindService;

    public MeasureService(MeasureMapper mapper, MeasureRepository measureRepository,
            BeehiveRepository beehiveRepository, AudioService audioService, BeehiveMindService beehiveMindService) {
        this.mapper = mapper;
        this.measureRepository = measureRepository;
        this.beehiveRepository = beehiveRepository;
        this.audioService = audioService;
        this.beehiveMindService = beehiveMindService;
    }

    @Override
    public List<MeasureDto> getAll() {
        return measureRepository.findAll().stream()
                .map(mapper::getDto)
                .toList();
    }

    @Override
    public Optional<MeasureDto> getById(Long id) {
        return measureRepository.findById(id)
                .map(mapper::getDto);
    }

    @Override
    @Transactional
    public MeasureDto create(MeasureForm form) {
        Measure entity = mapper.getEntity(form);

        Beehive beehive = beehiveRepository.findBySerial(form.getBeehiveSerial())
                .orElseThrow(() -> new DependencyNotFoundException(
                        "Beehive with serial " + form.getBeehiveSerial() + " not found."));
        entity.setBeehive(beehive);

        String filePath;
        try {
            filePath = audioService.saveFile(form.getAudioRecording());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save or convert file", e);
        }
        ClassificationResult classificationResult = beehiveMindService.registerAudio(filePath, form.getLabel());
        entity.setAudioRecordingUrl(filePath);
        entity.setLabel(classificationResult.getLabel());

        Measure savedEntity = measureRepository.save(entity);

        return mapper.getDto(savedEntity);
    }

    @Override
    public Optional<MeasureDto> update(Long id, MeasureForm form) {
        if (!measureRepository.existsById(id)) {
            return Optional.empty();
        }
        Measure entity = mapper.getEntity(form);

        Beehive beehive = beehiveRepository.findBySerial(form.getBeehiveSerial())
                .orElseThrow(() -> new DependencyNotFoundException(
                        "Beehive with Serial " + form.getBeehiveSerial() + " not found."));
        entity.setBeehive(beehive);

        entity.setId(id);
        Measure updatedEntity = measureRepository.save(entity);
        return Optional.of(mapper.getDto(updatedEntity));
    }

    @Override
    public boolean delete(Long id) {
        if (!measureRepository.existsById(id)) {
            return false;
        }
        measureRepository.deleteById(id);
        return true;
    }

    public List<MeasureDto> findMeasuresByBeehiveId(Long beehiveId) {
        return measureRepository.findByBeehive_Id(beehiveId)
                .stream()
                .map(mapper::getDto) // Adjust to the correct mapping method
                .toList();
    }
}
