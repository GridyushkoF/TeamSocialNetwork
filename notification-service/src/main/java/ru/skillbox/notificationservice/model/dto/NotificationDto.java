package ru.skillbox.notificationservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.commonlib.event.notification.NotificationType;

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
