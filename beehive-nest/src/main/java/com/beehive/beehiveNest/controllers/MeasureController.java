package com.beehive.beehiveNest.controllers;

import com.beehive.beehiveNest.model.dtos.MeasureDto;
import com.beehive.beehiveNest.model.forms.MeasureForm;
import com.beehive.beehiveNest.services.MeasureService;
import com.beehive.beehiveNest.services.S3AudioService;

import org.springframework.core.io.AbstractFileResolvingResource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    private S3AudioService s3AudioService;

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
    public ResponseEntity<Resource> serveAudio(@PathVariable String fileName) {
        try {
            // Retrieve file bytes from S3 (implement getFile in your S3AudioService)
            byte[] data = s3AudioService.getFile(fileName);

            if (data == null || data.length == 0) {
                return ResponseEntity.notFound().build();
            }

            // Wrap the bytes in a Resource
            Resource resource = new ByteArrayResource(data);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (Exception e) {
            // Log error as needed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
