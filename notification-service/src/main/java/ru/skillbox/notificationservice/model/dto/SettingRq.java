package ru.skillbox.notificationservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.notificationservice.model.enums.NotificationType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SettingRq {

    private boolean enable;
    private NotificationType notificationType;

}
