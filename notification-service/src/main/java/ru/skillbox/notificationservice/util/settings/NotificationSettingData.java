package ru.skillbox.notificationservice.util.settings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.notificationservice.model.entity.NotificationType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSettingData {

    private boolean enable;
    private NotificationType notificationType;

}
