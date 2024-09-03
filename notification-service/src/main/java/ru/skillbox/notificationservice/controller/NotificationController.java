package ru.skillbox.notificationservice.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.notificationservice.model.dto.*;
import ru.skillbox.notificationservice.service.NotificationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.apiPrefix}")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/settings")
    public ResponseEntity<NotificationSettingsDto> getNotificationSetting(
            @RequestHeader("id") Long currentAuthUserId
    ) {
        return ResponseEntity.ok(notificationService.getSettings(currentAuthUserId));
    }

    @PutMapping("/settings")
    public ResponseEntity<NotificationSettingsDto> updateNotificationSettings(
            @RequestBody SettingRq settingsData,
            @RequestHeader("id") Long currentAuthUserId) {
        return ResponseEntity.ok(notificationService.updateSettings(settingsData, currentAuthUserId));
    }

    @PostMapping("/settings")
    public ResponseEntity<NotificationSettingsDto> createNotificationSetting(
            @RequestBody SettingsDto settingsDto,
            @RequestHeader("id") Long currentAuthUserId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService
                .createSettings(settingsDto, currentAuthUserId));
    }


    @GetMapping
    public ResponseEntity<NotificationSentDto> getNotification(
            @RequestHeader("id") Long currentAuthUserId
    ) {

        return ResponseEntity.ok(notificationService.getNotifications(currentAuthUserId));
    }


    @PostMapping
    public ResponseEntity<NotificationDto> createNotification(
            @RequestBody NotificationInputDto notificationInputDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.createNotification(notificationInputDto));
    }

    @GetMapping("/count")
    public ResponseEntity<NotificationCountRs> getNotificationCount(
            @RequestHeader("id") Long currentAuthUserId) {
        return ResponseEntity.ok(notificationService.getCount(currentAuthUserId));
    }

    @PutMapping("/readed")
    @ResponseStatus(HttpStatus.OK)
    public void setReaded(
            @RequestHeader("id") Long currentAuthUserId
    ) {
        notificationService.setReaded(currentAuthUserId);
    }
}
