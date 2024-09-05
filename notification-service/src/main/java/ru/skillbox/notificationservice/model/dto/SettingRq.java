package ru.skillbox.notificationservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.commonlib.event.notification.NotificationType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SettingRq {

    @Schema(description = "Enable setting", example = "true")
    private boolean enable;

    @Schema(description = "Type of notification", example = "EMAIL")
    private NotificationType notificationType;

}
