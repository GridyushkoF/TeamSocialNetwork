package ru.skillbox.userservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Log4j2
public class StorageService {

    private String getProjectRoot() {
        return System.getProperty("user.dir");
    }

    private String getImagesDirectory() {
        return getProjectRoot() + "/images";
    }

    public String loadImageToStorage(MultipartFile file) {
        String fileId = UUID.randomUUID().toString();
        String imagesPath = getImagesDirectory();
        File imagesDirectory = new File(imagesPath);

        if (!imagesDirectory.exists()) {
            if (imagesDirectory.mkdirs()) {
                log.info("Directory created: " + imagesPath);
            } else {
                log.error("Failed to create directory: " + imagesPath);
                return null;
            }
        }
        String fileName = fileId + getExtensionFromFilename(file.getOriginalFilename());
        String filePath = imagesPath + "/" + fileName;
        try {
            file.transferTo(new File(filePath));
            log.info("File saved to: " + filePath);
        } catch (IOException e) {
            log.error("Error saving file: " + e.getMessage(), e);
        }
        return "/api/v1/img/" + fileName;
    }

    public String getExtensionFromFilename(String filename) {
        return filename.substring(filename.lastIndexOf('.'));
    }
}
