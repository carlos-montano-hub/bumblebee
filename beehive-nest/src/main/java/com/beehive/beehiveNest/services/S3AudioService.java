package com.beehive.beehiveNest.services;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Service
public class S3AudioService {
    @Value("${s3.clean-bucket-name}")
    private String cleanBucketName;
    @Value("${s3.raw-bucket-name}")
    private String rawBucketName;

    private final S3Client s3Client;

    public S3AudioService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String saveFile(MultipartFile audioFile, String filename) {
        try {
            InputStream inputStream = audioFile.getInputStream();

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(rawBucketName)
                    .key(filename)
                    .contentType(audioFile.getContentType())
                    .build();

            PutObjectResponse response = s3Client.putObject(request,
                    software.amazon.awssdk.core.sync.RequestBody.fromInputStream(inputStream, audioFile.getSize()));
            System.out.println("response:\n" + response);
            System.out.println(
                    s3Client.utilities().getUrl(builder -> builder.bucket(rawBucketName).key(filename)).toString());
            return s3Client.utilities().getUrl(builder -> builder.bucket(rawBucketName).key(filename)).toString();

        } catch (Exception e) {
            throw new RuntimeException("Error uploading file to S3 storage", e);
        }
    }

    public byte[] getFile(String filename) {
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(rawBucketName)
                    .key(filename)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(request);
            return s3Object.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Error reading file from S3 storage", e);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching file from S3 storage", e);
        }
    }
}
