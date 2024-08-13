package ru.skillbox.notificationservice.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public ResponseEntity<NotificationSettingsDto> getNotificationSetting(HttpServletRequest request){

        return ResponseEntity.ok(notificationService.getSettings(request));

    }

//    @PutMapping("/settings")
//    public ResponseEntity<NotificationSettingsDto> updateNotificationSettings(
//            @RequestBody SettingRq settingsData, HttpServletRequest request){
//        return ResponseEntity.ok(notificationService.updateSettings(settingsData, request));
//    }

    @PutMapping("/settings")
    public ResponseEntity<NotificationSettingsDto> updateNotificationSettings(
            @RequestBody SettingRq settingsData, HttpServletRequest request) {
        NotificationSettingsDto updatedSettings = notificationService.updateSettings(settingsData, request);
        return ResponseEntity.ok(updatedSettings);
    }

//    @PostMapping("/settings")
//    public ResponseEntity<NotificationSettingsDto> createNotificationSetting(
//            @RequestBody SettingsDto settingsDto, HttpServletRequest request){
//        return ResponseEntity.ok(notificationService.createSettings(settingsDto, request));
//    }

    @PostMapping("/settings")
    public ResponseEntity<NotificationSettingsDto> createNotificationSetting(
            @RequestBody SettingsDto settingsDto, HttpServletRequest request) {
        NotificationSettingsDto createdSettings = notificationService.createSettings(settingsDto, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSettings);
    }

    @GetMapping
    public ResponseEntity<NotificationSentDto> getNotification(HttpServletRequest request) {

        return ResponseEntity.ok(notificationService.getNotifications(request));
    }

   // @PostMapping
    //public ResponseEntity<NotificationDto> createNotification(
           // @RequestBody NotificationInputDto notificationInputDto){
        // return ResponseEntity.ok(notificationService.createNotification(notificationInputDto))
   @PostMapping
   public ResponseEntity<NotificationDto> createNotification(
           @RequestBody NotificationInputDto notificationInputDto) {
       NotificationDto createdNotification = notificationService.createNotification(notificationInputDto);
       return new ResponseEntity<>(createdNotification, HttpStatus.CREATED);
   }


    @GetMapping("/count")
    public ResponseEntity<NotificationCountRs> getNotificationCount(HttpServletRequest request) {
        return ResponseEntity.ok(notificationService.getCount(request));
    }

    @PutMapping("/readed")
    @ResponseStatus(HttpStatus.OK)
    public void setReaded(HttpServletRequest request) {
        notificationService.setReaded(request);
    }
}
