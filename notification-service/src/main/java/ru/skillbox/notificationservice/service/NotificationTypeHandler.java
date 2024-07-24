package ru.skillbox.notificationservice.service;

import org.springframework.stereotype.Component;
import ru.skillbox.notificationservice.model.entity.NotificationType;
import ru.skillbox.notificationservice.util.settings.NotificationSetting;

@Component
public class NotificationTypeHandler {

    public boolean isEnabled(NotificationSetting notificationSetting, NotificationType type) {
        switch (type) {
            case POST:
                return notificationSetting.isPost();
            case MESSAGE:
                return notificationSetting.isMessage();
            case POST_COMMENT:
                return notificationSetting.isPostComment();
            case FRIEND_REQUEST:
                return notificationSetting.isFriendRequest();
            case FRIEND_BIRTHDAY:
                return notificationSetting.isFriendBirthday();
            case SEND_EMAIL_MESSAGE:
                return notificationSetting.isSendEmailMessage();
            case SEND_PHONE_MESSAGE:
                return notificationSetting.isSendPhoneMessage();
            case COMMENT_COMMENT:
                return notificationSetting.isCommentComment();
            default:
                return false;
        }
    }
}
