package ru.skillbox.userservice.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.userservice.service.StorageService;

import java.util.Map;


@RestController
@RequestMapping("/storage")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class StorageController {

    private final StorageService storageService;

    @PostMapping
    public ResponseEntity<Map<String,String>> uploadFile(@ModelAttribute MultipartFile file,
                                        @RequestParam("type") String fileType) {
        return ResponseEntity.ok(Map.of("fileName",storageService.uploadFileAndGetLink(file)));
    }
}
