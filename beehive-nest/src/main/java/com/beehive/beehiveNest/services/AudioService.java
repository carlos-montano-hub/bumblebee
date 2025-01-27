package com.beehive.beehiveNest.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class AudioService {

    public String saveFile(MultipartFile multipartFile) {
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
                "-acodec", "pcm_s16le",  // Standard WAV codec
                "-ar", "44100",          // Sample rate: 44.1kHz
                "-ac", "2",              // Stereo audio (2 channels)
                outputPath
        );

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
}

