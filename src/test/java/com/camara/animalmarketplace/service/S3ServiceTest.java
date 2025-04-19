package com.camara.animalmarketplace.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class S3ServiceTest {

    @Mock
    private S3Client s3Client;

    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        s3Service = new S3Service(s3Client);
    }

    /**
     * Test de la méthode uploadFile de S3Service.
     * @throws Exception
     */
    @Test
    void testUploadFile() throws Exception {
        // Charger un fichier depuis src/test/resources
        InputStream inputStream = getClass().getResourceAsStream("/test-file.txt");
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("test-file.txt");
        when(mockFile.getInputStream()).thenReturn(inputStream);

        s3Service.uploadFile(mockFile);

        ArgumentCaptor<PutObjectRequest> captor = ArgumentCaptor.forClass(PutObjectRequest.class);
        verify(s3Client).putObject(captor.capture(), eq(Path.of("test-file.txt")));

        assertEquals("test-file.txt", captor.getValue().key());
    }

    /**
     * Test de la méthode downloadFile de S3Service.
     */
    @Test
    void testDownloadFile() {
        String fileName = "test-file.txt";

        s3Service.downloadFile(fileName);

        ArgumentCaptor<GetObjectRequest> captor = ArgumentCaptor.forClass(GetObjectRequest.class);
        verify(s3Client).getObject(captor.capture(), eq(Path.of(fileName)));

        assertEquals("test-file.txt", captor.getValue().key());
    }

    /**
     * Test de la méthode deleteFile de S3Service.
     */
    @Test
    void testDeleteFile() {
        String fileName = "test-file.txt";

        s3Service.deleteFile(fileName);

        ArgumentCaptor<DeleteObjectRequest> captor = ArgumentCaptor.forClass(DeleteObjectRequest.class);
        verify(s3Client).deleteObject(captor.capture());

        assertEquals("test-file.txt", captor.getValue().key());
    }
}