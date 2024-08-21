package ru.skillbox.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.userservice.service.StorageService;

import java.util.Map;


@RestController
@RequestMapping("/storage")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @PostMapping
    public ResponseEntity<?> uploadFile(@ModelAttribute MultipartFile file,
                                        @RequestParam("type") String fileType) {
        return ResponseEntity.ok(Map.of("fileName",storageService.uploadFileAndGetLink(file)));
    }
}
