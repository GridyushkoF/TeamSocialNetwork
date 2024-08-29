package ru.skillbox.commonlib.event.notification;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
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
