package com.beehive.beehiveNest.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.beehive.beehiveNest.model.InternalModels.AudioResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class AudioService {
    private final RestTemplate restTemplate;

    public AudioService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String saveFile(MultipartFile multipartFile) throws IOException {
        saveOnStaticStorage(multipartFile);
        String relativePath = "uploads/audio/";
        String uploadDir = System.getProperty("user.dir") + "/" + relativePath;
        String originalFileName = multipartFile.getOriginalFilename();
        assert originalFileName != null;
        String fileExtension = getFileExtension(originalFileName);
        String fileName = UUID.randomUUID() + fileExtension;
        File file = new File(uploadDir + fileName);

        try {
            Files.createDirectories(file.getParentFile().toPath());
            multipartFile.transferTo(file);

            if (!isWavFile(fileExtension)) {
                // Convert to WAV
                String wavFileName = fileName.substring(0, fileName.lastIndexOf('.')) + ".wav";
                String wavFilePath = uploadDir + wavFileName;
                convertToWav(file.getAbsolutePath(), wavFilePath);

                // Delete the original non-WAV file
                Files.deleteIfExists(file.toPath());

                return wavFileName;
            } else {
                // If it's already a WAV file, just return its filename
                return fileName;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save or convert file", e);
        }
    }

    private boolean isWavFile(String fileExtension) {
        return ".wav".equalsIgnoreCase(fileExtension);
    }

    private String getFileExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // Empty extension
        }
        return fileName.substring(lastIndexOf);
    }

    private void convertToWav(String inputPath, String outputPath) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "ffmpeg",
                "-i", inputPath,
                "-acodec", "pcm_s16le", // Standard WAV codec
                "-ar", "44100", // Sample rate: 44.1kHz
                "-ac", "2", // Stereo audio (2 channels)
                outputPath);

        Process process = processBuilder.start();
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("FFmpeg conversion failed with exit code: " + exitCode);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("FFmpeg conversion interrupted", e);
        }
    }

    public String saveOnStaticStorage(MultipartFile audioFile) throws IOException {
        String uploadUrl = "http://mini0-service/audio/upload"; // mini0 endpoint
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new AudioResource(
                audioFile.getInputStream(),
                audioFile.getOriginalFilename()));

        HttpHeaders headers = new HttpHeaders();
        // headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        // Optionally add API key header if required by mini0 service
        // headers.set("X-API-Key", "YOUR_API_KEY");

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(uploadUrl, requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            // Assuming mini0 returns the file path as plain text
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to upload file to mini0 service");
        }
    }
}
