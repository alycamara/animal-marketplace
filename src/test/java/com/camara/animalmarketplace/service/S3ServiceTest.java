package com.camara.animalmarketplace.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Disabled // Désactive tous les tests de cette classe
class S3ServiceTest {

    @Mock
    private S3Client s3Client;


    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        // Initialisation des mocks avant chaque test
        MockitoAnnotations.openMocks(this);
        s3Service = new S3Service(s3Client);
    }

    @Test
    void testDownloadFile() {
        // Test du téléchargement d'un fichier existant
        String fileName = "test-file.txt";

        s3Service.downloadFile(fileName);

        // Capture de la requête pour vérifier les paramètres
        ArgumentCaptor<GetObjectRequest> captor = ArgumentCaptor.forClass(GetObjectRequest.class);
        verify(s3Client).getObject(captor.capture(), eq(Path.of(fileName)));

        // Vérifie que le nom du fichier est correct
        assertEquals(fileName, captor.getValue().key());
    }

    @Test
    void testDownloadFile_FileNotFound() {
        // Test du téléchargement d'un fichier inexistant
        String fileName = "nonexistent-file.txt";

        doThrow(NoSuchKeyException.builder().build())
                .when(s3Client).getObject(any(GetObjectRequest.class), any(Path.class));

        // Vérifie que l'exception est levée
        assertThrows(NoSuchKeyException.class, () -> s3Service.downloadFile(fileName));
    }

    @Test
    void testUploadFile() throws Exception {
        // Test du chargement d'un fichier
        InputStream inputStream = getClass().getResourceAsStream("/test-file.txt");
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("test-file.txt");
        when(mockFile.getInputStream()).thenReturn(inputStream);
        when(mockFile.getSize()).thenReturn(1024L);

        String fileUrl = s3Service.uploadFile(mockFile);

        // Capture de la requête pour vérifier les paramètres
        ArgumentCaptor<PutObjectRequest> captor = ArgumentCaptor.forClass(PutObjectRequest.class);
        verify(s3Client).putObject(captor.capture(), any(RequestBody.class));

        // Vérifie que le nom du fichier et l'URL sont corrects
        assertEquals("test-file.txt", captor.getValue().key());
        assertEquals("https://null.s3.amazonaws.com/test-file.txt", fileUrl);
    }

    @Test
    void testUploadFileThrowsException() throws Exception {
        // Test du chargement d'un fichier avec une erreur
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("test-file.txt");
        when(mockFile.getInputStream()).thenThrow(new RuntimeException("IO Error"));

        // Vérifie que l'exception est levée
        RuntimeException exception = assertThrows(RuntimeException.class, () -> s3Service.uploadFile(mockFile));

        // Vérifie le message de l'exception
        assertEquals("IO Error", exception.getMessage());
    }

    @Test
    void testDeleteFile() {
        // Test de la suppression d'un fichier existant
        String fileName = "test-file.txt";

        s3Service.deleteFile(fileName);

        // Capture de la requête pour vérifier les paramètres
        ArgumentCaptor<DeleteObjectRequest> captor = ArgumentCaptor.forClass(DeleteObjectRequest.class);
        verify(s3Client).deleteObject(captor.capture());

        // Vérifie que le nom du fichier est correct
        assertEquals(fileName, captor.getValue().key());
    }

    @Test
    void testDeleteFile_FileNotFound() {
        // Test de la suppression d'un fichier inexistant
        String fileName = "nonexistent-file.txt";

        doThrow(NoSuchKeyException.builder().build())
                .when(s3Client).deleteObject(any(DeleteObjectRequest.class));

        // Vérifie que l'exception est levée
        assertThrows(NoSuchKeyException.class, () -> s3Service.deleteFile(fileName));
    }

    @Test
    void testListFiles() {
        // Test de la récupération de la liste des fichiers
        ListObjectsV2Response response = mock(ListObjectsV2Response.class);
        S3Object object1 = S3Object.builder().key("file1.txt").build();
        S3Object object2 = S3Object.builder().key("file2.txt").build();
        when(response.contents()).thenReturn(List.of(object1, object2));
        when(s3Client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(response);

        List<String> fileNames = s3Service.listFiles();

        // Vérifie que la liste contient les bons fichiers
        assertEquals(2, fileNames.size());
        assertEquals("file1.txt", fileNames.get(0));
        assertEquals("file2.txt", fileNames.get(1));
    }

    @Test
    void testListFiles_EmptyBucket() {
        // Test de la récupération de la liste des fichiers avec un bucket vide
        ListObjectsV2Response response = mock(ListObjectsV2Response.class);
        when(response.contents()).thenReturn(List.of());
        when(s3Client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(response);

        List<String> fileNames = s3Service.listFiles();

        // Vérifie que la liste est vide
        assertTrue(fileNames.isEmpty());
    }
}