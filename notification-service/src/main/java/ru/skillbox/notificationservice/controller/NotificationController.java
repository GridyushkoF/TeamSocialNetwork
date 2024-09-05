package ru.skillbox.notificationservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.notificationservice.model.dto.*;
import ru.skillbox.notificationservice.service.NotificationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.apiPrefix}")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Notification Controller", description = "Notification API")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/settings")
    @Operation(summary = "Get notification setting")
    public ResponseEntity<NotificationSettingsDto> getNotificationSetting(
            @RequestHeader("id") Long currentAuthUserId) {
        return ResponseEntity.ok(notificationService.getSettings(currentAuthUserId));
    }

    @PutMapping("/settings")
    @Operation(summary = "Update notification setting")
    public ResponseEntity<NotificationSettingsDto> updateNotificationSettings(
            @RequestBody SettingRq settingsData,
            @RequestHeader("id") Long currentAuthUserId) {
        return ResponseEntity.ok(notificationService.updateSettings(settingsData, currentAuthUserId));
    }

    @PostMapping("/settings")
    @Operation(summary = "Create notification setting")
    public ResponseEntity<NotificationSettingsDto> createNotificationSetting(
            @RequestBody SettingsDto settingsDto,
            @RequestHeader("id") Long currentAuthUserId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService
                .createSettings(settingsDto, currentAuthUserId));
    }


    @GetMapping
    @Operation(summary = "Get notification")
    public ResponseEntity<NotificationSentDto> getNotification(
            @RequestHeader("id") Long currentAuthUserId
    ) {
        return ResponseEntity.ok(notificationService.getNotifications(currentAuthUserId));
    }


    @PostMapping
    @Operation(summary = "Create notification")
    public ResponseEntity<NotificationDto> createNotification(
            @RequestBody NotificationInputDto notificationInputDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.createNotification(notificationInputDto));
    }

    @GetMapping("/count")
    @Operation(summary = "Get notification count")
    public ResponseEntity<NotificationCountRs> getNotificationCount(
            @RequestHeader("id") Long currentAuthUserId) {
        return ResponseEntity.ok(notificationService.getCount(currentAuthUserId));
    }

    @PutMapping("/readed")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Set readed")
    public void setReaded(
            @RequestHeader("id") Long currentAuthUserId
    ) {
        notificationService.setReaded(currentAuthUserId);
    }
}
