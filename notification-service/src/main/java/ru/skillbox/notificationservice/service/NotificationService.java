package ru.skillbox.notificationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.commonlib.event.notification.NotificationStatus;
import ru.skillbox.notificationservice.exception.SettingsAlreadyExistsException;
import ru.skillbox.notificationservice.mapper.v1.NotificationMapperV1;
import ru.skillbox.notificationservice.mapper.v1.SettingsMapperV1;
import ru.skillbox.notificationservice.model.dto.*;
import ru.skillbox.notificationservice.model.entity.Notification;
import ru.skillbox.notificationservice.model.entity.Settings;
import ru.skillbox.notificationservice.repository.NotificationRepository;
import ru.skillbox.notificationservice.repository.SettingsRepository;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SettingsRepository settingsRepository;
    private final NotificationRepository notificationRepository;
    private final SettingsMapperV1 settingsMapper;
    private final NotificationMapperV1 notificationMapper;


    @Transactional
    public NotificationSettingsDto getSettings(Long currentAuthUserId) {
        Settings settings;
        Optional<Settings> settingsOptional =
                settingsRepository.findByUserId(currentAuthUserId);
        settings = settingsOptional.orElseGet(() -> createNewSettings(currentAuthUserId));
        return settingsMapper.toDto(settings);
    }

    private Settings createNewSettings(Long currentAuthUserId) {
        Settings newSettings = Settings.builder()
                .userId(currentAuthUserId)
                .build();
        return settingsRepository.saveAndFlush(newSettings);
    }

    @Transactional
    public NotificationSettingsDto createSettings(SettingsDto settingsDto, Long currentAuthUserId) {
        if (settingsRepository.findByUserId(currentAuthUserId).isPresent()) {
            throw new SettingsAlreadyExistsException(currentAuthUserId);
        }
        Settings newSettings = settingsMapper.toEntity(currentAuthUserId, settingsDto);
        return settingsMapper.toDto(settingsRepository.save(newSettings));
    }

    @Transactional
    public NotificationSettingsDto updateSettings(SettingRq settingRq,
                                                  Long currentAuthUserId) {
        Settings settings =
                settingsRepository.findByUserId(currentAuthUserId).orElseThrow(
                        () -> new NoSuchElementException("No such notification settings existed!")
                );

        switch (settingRq.getNotificationType()) {
            case POST -> settings.setPost(settingRq.isEnable());
            case MESSAGE -> settings.setMessage(settingRq.isEnable());
            case FRIEND_BIRTHDAY -> settings.setFriendBirthday(settingRq.isEnable());
            case FRIEND_REQUEST -> settings.setFriendRequest(settingRq.isEnable());
            case POST_COMMENT -> settings.setPostComment(settingRq.isEnable());
            case COMMENT_COMMENT -> settings.setCommentComment(settingRq.isEnable());
            case SEND_EMAIL_MESSAGE -> settings.setSendEmailMessage(settingRq.isEnable());
        }

        return settingsMapper.toDto(settingsRepository.save(settings));
    }

    @Transactional
    public NotificationDto createNotification(NotificationInputDto notificationInputDto) {
        Notification notification = notificationMapper.toEntity(notificationInputDto);
        notification.setNotificationStatus(NotificationStatus.SENT);

        return notificationMapper.toNotificationDto(notificationRepository.save(notification));
    }

    public NotificationSentDto getNotifications(long currentAuthUserId) {
        return notificationMapper.toResponse(
                notificationRepository.findAllByUserIdAndNotificationStatus(currentAuthUserId, NotificationStatus.SENT));
    }

    public NotificationCountRs getCount(long currentAuthUserId) {
        return notificationMapper.toCountResponse(
                notificationRepository.
                        findAllByUserIdAndNotificationStatus(currentAuthUserId, NotificationStatus.SENT).size());
    }

    @Transactional
    public void setReaded(long currentAuthUserId) {
        notificationRepository
                .findAllByUserIdAndNotificationStatus(currentAuthUserId, NotificationStatus.SENT)
                .forEach(notification -> {
                    notification.setNotificationStatus(NotificationStatus.READ);
                    notification.setTime(LocalDateTime.now());
                    notificationRepository.save(notification);
                });
    }
}
