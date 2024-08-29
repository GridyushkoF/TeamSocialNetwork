package ru.skillbox.notificationservice.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skillbox.commonlib.event.notification.NotificationEvent;
import ru.skillbox.notificationservice.model.dto.NotificationInputDto;
import ru.skillbox.notificationservice.model.entity.Settings;
import ru.skillbox.notificationservice.repository.SettingsRepository;
import ru.skillbox.notificationservice.service.NotificationService;

@Component
public class NotificationEventConsumer implements EventConsumer<NotificationEvent> {

    private final SettingsRepository settingsRepository;
    private final NotificationService notificationService;

    @Autowired
    public NotificationEventConsumer(SettingsRepository settingsRepository, NotificationService notificationService) {
        this.settingsRepository = settingsRepository;
        this.notificationService = notificationService;
    }

    @Override
    public void consumeEvent(NotificationEvent event) {
        createNotifications(event);
    }

    private void createNotifications(NotificationEvent event) {
        for (Settings settings : settingsRepository.findAll()) {
            if (event.getAuthorId().equals(settings.getUserId())) continue;
            boolean haveToSendNotification = false;
            switch (event.getNotificationType()) {
                case POST -> haveToSendNotification = settings.isPost();
                case POST_COMMENT -> haveToSendNotification = settings.isPostComment();
                case COMMENT_COMMENT -> haveToSendNotification = settings.isCommentComment();
                case MESSAGE -> haveToSendNotification = settings.isMessage();
                case SEND_EMAIL_MESSAGE -> haveToSendNotification = settings.isSendEmailMessage();
                case FRIEND_BIRTHDAY -> haveToSendNotification = settings.isFriendBirthday();
                case FRIEND_REQUEST -> haveToSendNotification = settings.isFriendRequest();
            }
            if (haveToSendNotification) {
                NotificationInputDto notificationDto = NotificationInputDto.builder()
                        .notificationType(event.getNotificationType())
                        .content(event.getContent())
                        .authorId(event.getAuthorId())
                        .userId(settings.getUserId())
                        .build();
                notificationService.createNotification(notificationDto);
            }
        }
    }
}
