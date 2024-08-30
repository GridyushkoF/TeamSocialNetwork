package ru.skillbox.authentication.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.authentication.model.dto.RecoveryPasswordRequest;
import ru.skillbox.authentication.model.dto.SetPasswordRequest;
import ru.skillbox.authentication.model.dto.SimpleResponse;
import ru.skillbox.authentication.service.PasswordService;

@RestController
@RequestMapping("/password")
@RequiredArgsConstructor
@Slf4j
public class PasswordResetController {

    private final PasswordService passwordService;

    @PostMapping("/recovery/")
    public ResponseEntity<SimpleResponse> resetPassword(
            @RequestBody RecoveryPasswordRequest request) {
        return ResponseEntity.ok(passwordService.sendToEmail(request));
    }

    @PostMapping("/recovery/{recoveryLink}")
    public ResponseEntity<SimpleResponse> resetPasswordFromMessage(
            @PathVariable String recoveryLink,
            @RequestBody SetPasswordRequest request) {
        return ResponseEntity.ok(passwordService.setNewPassword(recoveryLink, request));
    }
}
