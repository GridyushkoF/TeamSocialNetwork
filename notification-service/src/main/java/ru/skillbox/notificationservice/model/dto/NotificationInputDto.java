package ru.skillbox.notificationservice.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.commonlib.notification.NotificationType;

@Getter
@Setter
@Builder
public class NotificationInputDto {
    private Long authorId;
    private Long userId;
    private NotificationType notificationType;
    private String content;
}
