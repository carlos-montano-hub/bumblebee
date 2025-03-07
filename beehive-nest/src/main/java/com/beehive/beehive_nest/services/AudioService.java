package com.beehive.beehive_nest.services;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class AudioService {

    private S3AudioService s3AudioService;

    public AudioService(S3AudioService s3AudioService) {
        this.s3AudioService = s3AudioService;
    }

    public String saveFile(MultipartFile multipartFile) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        assert originalFileName != null;
        String fileExtension = getFileExtension(originalFileName);
        String fileId = UUID.randomUUID().toString();

        byte[] fileBytes = multipartFile.getBytes();
        byte[] wavBytes = fileBytes;

        if (!isWavFile(fileExtension)) {
            wavBytes = convertToWav(fileBytes);
        }

        MultipartFile convertedMultipart = new MockMultipartFile(
                fileId + ".wav",
                fileId + ".wav",
                "audio/wav",
                new ByteArrayInputStream(wavBytes));

        s3AudioService.saveFile(convertedMultipart, fileId);
        return fileId;
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

    private byte[] convertToWav(byte[] inputBytes) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ProcessBuilder processBuilder = new ProcessBuilder(
                "ffmpeg", "-i", "-", "-acodec", "pcm_s16le", "-ar", "44100", "-ac", "2", "-f", "wav", "-");

        Process process = processBuilder.start();

        try (InputStream processInput = process.getInputStream();
                InputStream processError = process.getErrorStream()) {

            process.getOutputStream().write(inputBytes);
            process.getOutputStream().close();

            IOUtils.copy(processInput, outputStream);

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("FFmpeg conversion failed: ");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("FFmpeg conversion interrupted", e);
        }

        return outputStream.toByteArray();
    }
}
