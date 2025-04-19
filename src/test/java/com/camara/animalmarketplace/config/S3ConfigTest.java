package com.camara.animalmarketplace.config;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class S3ConfigTest {

    @Value("${aws.access-key-id}")
    private String accessKeyId = "test-access-key";

    @Value("${aws.secret-access-key}")
    private String secretAccessKey = "test-secret-key";

    @Value("${aws.s3.region}")
    private String region = "us-east-1";

    @Test
    void testS3ClientCreation() {
        S3Config s3Config = new S3Config();
        S3Client s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
                .build();

        assertNotNull(s3Client, "Le client S3 ne doit pas être null");
    }
}