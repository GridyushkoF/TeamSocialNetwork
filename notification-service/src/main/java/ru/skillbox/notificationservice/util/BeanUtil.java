package ru.skillbox.notificationservice.util;

import lombok.experimental.UtilityClass;
import org.springframework.beans.BeanUtils;
import ru.skillbox.notificationservice.model.dto.NotificationCreateDto;
import ru.skillbox.notificationservice.model.dto.NotificationSettingDto;
import ru.skillbox.notificationservice.model.entity.NotificationCreate;
import ru.skillbox.notificationservice.util.settings.NotificationSetting;

@UtilityClass
public class BeanUtil {

    public void copyPropertiesFromDto(NotificationSettingDto dto, NotificationSetting notificationSetting) {
        try {
            BeanUtils.copyProperties(dto, notificationSetting);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copyPropertiesFromCreatDto(NotificationCreateDto dto, NotificationCreate notificationCreate) {
        try {
            BeanUtils.copyProperties(dto, notificationCreate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
