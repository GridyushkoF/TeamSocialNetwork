package ru.skillbox.userservice.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.userservice.model.dto.AccountDto;

import java.util.concurrent.CompletableFuture;

/**
 *
 */
public interface StorageService {
    /**
     *
     * @param file
     * @return
     */
    CompletableFuture<AccountDto> loadImageToStorage(MultipartFile file);
}
