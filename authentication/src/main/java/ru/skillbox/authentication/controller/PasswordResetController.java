package ru.skillbox.authentication.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Password Reset Controller", description = "Password Reset API")
public class PasswordResetController {

    private final PasswordService passwordService;

    @PostMapping("/recovery/")
    @Operation(summary = "Reset password")
    public ResponseEntity<SimpleResponse> resetPassword(
            @RequestBody RecoveryPasswordRequest request) {
        return ResponseEntity.ok(passwordService.sendToEmail(request));
    }

    @PostMapping("/recovery/{recoveryLink}")
    @Operation(summary = "Reset password from message")
    public ResponseEntity<SimpleResponse> resetPasswordFromMessage(
            @PathVariable String recoveryLink,
            @RequestBody SetPasswordRequest request) {
        return ResponseEntity.ok(passwordService.setNewPassword(recoveryLink, request));
    }
}
