package ru.skillbox.notificationservice.model.dto;

import lombok.Getter;
import lombok.Setter;
import ru.skillbox.notificationservice.model.entity.NotificationType;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationCreateDto {
    private Long id;
    private LocalDateTime time;
    private Long authorId;
    private Long userId;
    private String content;
    private NotificationType notificationType;
    private boolean isStatusSent;
}
