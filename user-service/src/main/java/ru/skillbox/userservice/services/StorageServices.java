package ru.skillbox.userservice.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.userservice.exceptions.BadRequestException;
import ru.skillbox.userservice.model.dto.AccountDto;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
public class StorageServices {

    // private final ImageRepository imageRepository;


    @Async
    @Transactional
    public CompletableFuture<AccountDto> loadImageToStorage(MultipartFile file) {
        if (file.isEmpty()) {
            return CompletableFuture.failedFuture(
                    new BadRequestException("Empty file")
            );
        }
        //Image image = new Image();
        //image.setImageTitle(file.getOriginalFilename());
        if (Objects.requireNonNull(file.getContentType()).contains("png")) {
            //image.setImageType(ImageType.PNG);
        } else if (Objects.requireNonNull(file.getContentType()).contains("jpeg")) {
            //image.setImageType(ImageType.JPEG);
        } else {
            return CompletableFuture.failedFuture(
                    new BadRequestException("incorrect file type")
            );
        }
        try {
            byte[] fileBytes = file.getBytes();
            //image.setImageBogy(fileBytes);
            return CompletableFuture.completedFuture(
                    new AccountDto()
                    //ResponseImageData.of(imageRepository.save(image))
            );
        } catch (IOException ex) {
            return CompletableFuture.failedFuture(
                    new BadRequestException("Could not store file " + file.getOriginalFilename() + ". Please try again!")
            );
        }
    }

}
