package com.camara.animalmarketplace.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * Service for managing files in an Amazon S3 bucket.
 * This service provides methods to upload, download, delete, and list files in the specified S3 bucket.
 **/

@Service
public class S3Service {
    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * Downloads a file from the specified S3 bucket.
     *
     * @param fileName the name of the file to download
     */
    public void downloadFile(String fileName) {
        logger.info("Téléchargement du fichier '{}' depuis le bucket '{}'", fileName, bucketName);
        s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build(),
                Paths.get(fileName)
        );
        logger.info("Téléchargement du fichier '{}' terminé avec succès", fileName);
    }


    /**
     * Uploads a file to the specified S3 bucket.
     *
     * @param file the file to upload
     * @return the URL of the uploaded file
     */
    public String uploadFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        logger.info("Chargement du fichier '{}' dans le bucket '{}'", fileName, bucketName);
        // Téléchargement du fichier en utilisant un flux d'entrée
        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            logger.info("Chargement du fichier '{}' terminé avec succès", fileName);
        } catch (IOException e) {
            logger.error("Erreur lors du chargement du fichier '{}'", fileName, e);
            throw new RuntimeException("Erreur lors du chargement du fichier", e);
        }
        // Construire l'URL publique de l'image
        String fileUrl = String.format("https://%s.s3.amazonaws.com/%s", bucketName, fileName);
        return fileUrl;
    }

    /**
     * Deletes a file from the specified S3 bucket.
     *
     * @param fileName the name of the file to delete
     */
    public void deleteFile(String fileName) {
        logger.info("Suppression du fichier '{}' du bucket '{}'", fileName, bucketName);
        s3Client.deleteObject(
                DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build()
        );
        logger.info("Suppression du fichier '{}' terminée avec succès", fileName);
    }

    /**
     * Lists all files in the specified S3 bucket.
     *
     * @return a list of file names
     */
    public List<String> listFiles() {
        logger.info("Récupération de la liste des fichiers du bucket '{}'", bucketName);

        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();

        ListObjectsV2Response response = s3Client.listObjectsV2(request);

        List<String> fileNames = response.contents().stream()
                .map(S3Object::key)
                .toList();

        fileNames.forEach(fileName -> logger.info("Fichier trouvé : {}", fileName));

        return fileNames;
    }


}