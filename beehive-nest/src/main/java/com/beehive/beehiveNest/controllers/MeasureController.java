package com.beehive.beehiveNest.controllers;

import com.beehive.beehiveNest.model.dtos.MeasureDto;
import com.beehive.beehiveNest.model.forms.MeasureForm;
import com.beehive.beehiveNest.services.MeasureService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/measure")
public class MeasureController {
    private final MeasureService measureService;
    private final String AUDIO_STORAGE_DIR = "uploads/audio/";

    public MeasureController(MeasureService service) {
        this.measureService = service; // Injecting into the controller as well
    }

    @GetMapping("/beehive/{beehiveId}")
    public List<MeasureDto> getMeasuresByBeehive(@PathVariable Long beehiveId) {
        return measureService.findMeasuresByBeehiveId(beehiveId);
    }

    @PostMapping
    public ResponseEntity<MeasureDto> createEntity(@ModelAttribute MeasureForm entityForm) {
        MeasureDto createdEntity = measureService.create(entityForm);
        return ResponseEntity.status(200).body(createdEntity);
    }

    @GetMapping("/audio/{fileName}")
    public ResponseEntity<Resource> serveAudio(@PathVariable String fileName) throws IOException {
        Path path = Paths.get(System.getProperty("user.dir") + "/" + AUDIO_STORAGE_DIR).resolve(fileName).normalize();
        Resource resource = new UrlResource(path.toUri());

        if (resource.exists()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
