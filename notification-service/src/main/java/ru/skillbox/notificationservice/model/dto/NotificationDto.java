package ru.skillbox.notificationservice.model.dto;

import lombok.*;
import ru.skillbox.commonlib.notification.NotificationType;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationDto {
    private Long id;
    private LocalDateTime time;
    private Long authorId;
    private Long userId;
    private String content;
    private NotificationType notificationType;
    private Boolean isStatusSent;
}
