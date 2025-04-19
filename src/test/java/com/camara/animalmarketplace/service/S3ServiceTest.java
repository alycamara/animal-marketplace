package com.camara.animalmarketplace.service;

import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void testDownloadFile() {
        String fileName = "test-file.txt";

        s3Service.downloadFile(fileName);

        ArgumentCaptor<GetObjectRequest> captor = ArgumentCaptor.forClass(GetObjectRequest.class);
        verify(s3Client).getObject(captor.capture(), eq(Path.of(fileName)));

        assertEquals(fileName, captor.getValue().key());
    }

    @Test
    void testUploadFile() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/test-file.txt");
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("test-file.txt");
        when(mockFile.getInputStream()).thenReturn(inputStream);
        when(mockFile.getSize()).thenReturn(1024L);

        String fileUrl = s3Service.uploadFile(mockFile);

        ArgumentCaptor<PutObjectRequest> captor = ArgumentCaptor.forClass(PutObjectRequest.class);
        verify(s3Client).putObject(captor.capture(), any(RequestBody.class));

        assertEquals("test-file.txt", captor.getValue().key());
        assertEquals("https://null.s3.amazonaws.com/test-file.txt", fileUrl);
    }

    @Test
    void testUploadFileThrowsException() throws Exception {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("test-file.txt");
        when(mockFile.getInputStream()).thenThrow(new RuntimeException("IO Error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> s3Service.uploadFile(mockFile));

        assertEquals("IO Error", exception.getMessage());
    }

    @Test
    void testDeleteFile() {
        String fileName = "test-file.txt";

        s3Service.deleteFile(fileName);

        ArgumentCaptor<DeleteObjectRequest> captor = ArgumentCaptor.forClass(DeleteObjectRequest.class);
        verify(s3Client).deleteObject(captor.capture());

        assertEquals(fileName, captor.getValue().key());
    }

    @Test
    void testListFiles() {
        ListObjectsV2Response response = mock(ListObjectsV2Response.class);
        S3Object object1 = S3Object.builder().key("file1.txt").build();
        S3Object object2 = S3Object.builder().key("file2.txt").build();
        when(response.contents()).thenReturn(List.of(object1, object2));
        when(s3Client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(response);

        List<String> fileNames = s3Service.listFiles();

        assertEquals(2, fileNames.size());
        assertEquals("file1.txt", fileNames.get(0));
        assertEquals("file2.txt", fileNames.get(1));
    }
}