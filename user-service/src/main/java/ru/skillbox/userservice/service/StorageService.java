package ru.skillbox.userservice.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
@Log4j2
@RequiredArgsConstructor
public class StorageService {

    private final AmazonS3 amazonS3Client;
    @Value("${yandex-cloud.bucket-name}")
    private String bucketName;

    public String uploadFileAndGetLink(MultipartFile multipartFile) {
        String fileUrl = "";
        try (InputStream inputStream = multipartFile.getInputStream()) {
            String originalFileName = multipartFile.getOriginalFilename();
            String hashedFilename = hashFilename(originalFileName);
            fileUrl = "https://storage.yandexcloud.net/" + bucketName + "/" + hashedFilename;
            uploadFileToS3Bucket(hashedFilename, inputStream, multipartFile.getSize());
        } catch (Exception e) {
            log.error("Error uploading file: ", e);
        }
        return fileUrl;
    }

    private String hashFilename(String filename) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(filename.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing filename", e);
        }
    }

    private void uploadFileToS3Bucket(String fileName, InputStream inputStream, long size) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(size);
            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, metadata));
            log.info("File uploaded successfully: " + fileName);
        } catch (AmazonServiceException e) {
            log.error("AmazonServiceException: ", e);
        } catch (Exception e) {
            log.error("Exception while uploading file: ", e);
        }
    }
}