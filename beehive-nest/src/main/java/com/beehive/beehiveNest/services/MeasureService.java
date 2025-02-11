package com.beehive.beehiveNest.services;

import com.beehive.beehiveNest.exceptions.DependencyNotFoundException;
import com.beehive.beehiveNest.model.dtos.MeasureDto;
import com.beehive.beehiveNest.model.entities.Beehive;
import com.beehive.beehiveNest.model.entities.Measure;
import com.beehive.beehiveNest.model.forms.MeasureForm;
import com.beehive.beehiveNest.model.mappers.MeasureMapper;
import com.beehive.beehiveNest.repository.BeehiveRepository;
import com.beehive.beehiveNest.repository.MeasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MeasureService implements CrudService<MeasureDto, MeasureForm> {

    @Autowired
    private MeasureMapper mapper;
    @Autowired
    private MeasureRepository measureRepository;
    @Autowired
    private BeehiveRepository beehiveRepository;
    @Autowired
    private AudioService audioService;

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
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("Failed to save or convert file", e);
        }
        entity.setAudioRecordingUrl(filePath);

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
                .collect(Collectors.toList());
    }
}
