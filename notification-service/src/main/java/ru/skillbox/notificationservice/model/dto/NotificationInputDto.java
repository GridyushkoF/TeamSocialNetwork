package ru.skillbox.notificationservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.commonlib.event.notification.NotificationType;

@Getter
@Setter
@Builder
@Schema(description = "DTO for creating a notification")
public class NotificationInputDto {

    @Schema(description = "Author ID of the notification")
    private Long authorId;

    @Schema(description = "User ID for whom the notification is intended")
    private Long userId;

    @Schema(description = "Type of notification")
    private NotificationType notificationType;

    @Schema(description = "Notification content")
    private String content;
}
