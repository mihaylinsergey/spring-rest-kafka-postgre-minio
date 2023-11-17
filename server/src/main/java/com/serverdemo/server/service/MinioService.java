package com.serverdemo.server.service;

import io.minio.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Optional;

@Service
@Slf4j
public class MinioService {
    private final MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucket;

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public String upload(File file, String name) throws Exception {
        try {
            createBucket();
        } catch (Exception e) {
            log.error("Image upload failed", e);
        }
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (Exception e) {
            log.error("Image upload failed", e);
        }
        saveImage(inputStream, name);
        return name;
    }

    @SneakyThrows
    private void createBucket() {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(bucket)
                .build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucket)
                    .build());
        }
    }

    @SneakyThrows
    private void saveImage(final InputStream inputStream,
                           final String fileName) {
        minioClient.putObject(PutObjectArgs.builder()
                .stream(inputStream, inputStream.available(), -1)
                .bucket(bucket)
                .object(fileName)
                .build());
    }

    public Optional<byte[]> getFile(String key) {
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucket)
                        .object(key)
                        .build())) {
            byte[] content = IOUtils.toByteArray(stream);
            return Optional.of(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
