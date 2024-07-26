package ru.skillbox.notificationservice.model.dto;

import lombok.Getter;
import lombok.Setter;
import ru.skillbox.notificationservice.model.enums.NotificationType;

@Getter
@Setter
public class NotificationInputDto {
    private Long authorId;
    private Long userId;
    private NotificationType notificationType;
    private String content;
}
