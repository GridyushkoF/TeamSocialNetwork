package ru.skillbox.notificationservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "DTO for a single notification")
public class NotificationDto {

    @Schema(description = "Notification ID")
    private Long id;

    @Schema(description = "Notification creation time")
    private LocalDateTime time;

    @Schema(description = "Author ID of the notification")
    private Long authorId;

    @Schema(description = "User ID for whom the notification is intended")
    private Long userId;

    @Schema(description = "Notification content")
    private String content;

    @Schema(description = "Type of notification")
    private NotificationType notificationType;

    @Schema(description = "Indicates whether the notification status has been sent")
    private Boolean isStatusSent;
}
