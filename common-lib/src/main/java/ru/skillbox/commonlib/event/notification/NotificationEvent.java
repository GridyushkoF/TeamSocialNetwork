package ru.skillbox.commonlib.event.notification;

import lombok.*;
import ru.skillbox.commonlib.event.Event;

@Data
@Builder

@ToString
public class NotificationEvent implements Event {
    private Long id;
    private Long authorId;
    private Long userId;
    private NotificationType notificationType;
    private String content;
}
