package ru.skillbox.notificationservice.service;

import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.commonlib.event.notification.NotificationStatus;
import ru.skillbox.commonlib.event.notification.NotificationType;
import ru.skillbox.notificationservice.exception.SettingsAlreadyExistsException;
import ru.skillbox.notificationservice.mapper.v1.SettingsMapperV1;
import ru.skillbox.notificationservice.model.dto.*;
import ru.skillbox.notificationservice.model.entity.Notification;
import ru.skillbox.notificationservice.model.entity.Settings;
import ru.skillbox.notificationservice.repository.NotificationRepository;
import ru.skillbox.notificationservice.repository.SettingsRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class NotificationServiceIT {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SettingsRepository settingsRepository;
    @Autowired
    private SettingsMapperV1 settingsMapperV1;
    @NonNull
    private static SettingsDto getTestSettingsDto() {
        SettingsDto settingsDto = new SettingsDto();
        settingsDto.setPost(true);
        settingsDto.setMessage(true);
        settingsDto.setFriendBirthday(true);
        settingsDto.setSendEmailMessage(true);
        settingsDto.setPostComment(true);
        settingsDto.setCommentComment(true);
        settingsDto.setSendEmailMessage(true);
        return settingsDto;
    }

    @BeforeEach
    void setUp() {
        settingsRepository.deleteAll();
        notificationRepository.deleteAll();
    }

    @Test
    @DisplayName("test getting settings, not creating before")
    void testGetSettings_correct_success() {
        NotificationSettingsDto notificationSettingsDto = notificationService.getSettings(1L);
        assertEquals(1, settingsRepository.findAll().size());
        assertEquals(new NotificationSettingsDto(), notificationSettingsDto);
    }

    @Test
    @DisplayName("test getting settings, creating before")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void testGetSettings_creatingSettingsBefore_success() {
        Settings settings = Settings.builder()
                .userId(1L)
                .post(true)
                .build();
        settingsRepository.save(settings);
        NotificationSettingsDto notificationSettingsDto = notificationService.getSettings(1L);
        assertEquals(1, settingsRepository.findAll().size());
        assertEquals(NotificationSettingsDto.builder().enablePost(true).build(),
                notificationSettingsDto);
    }

    @Test
    @DisplayName("testing create settings")
    void testCreateSettings_correct_success() {
        Long currentAuthUserId = 1L;
        SettingsDto settingsDto = getTestSettingsDto();

        NotificationSettingsDto createdSettings = notificationService.createSettings(settingsDto, currentAuthUserId);

        assertNotNull(createdSettings);
        assertTrue(createdSettings.isEnablePost());
        assertTrue(createdSettings.isEnableMessage());
        assertTrue(createdSettings.isEnableFriendBirthday());
        assertFalse(createdSettings.isEnableFriendRequest());
        assertTrue(createdSettings.isEnablePostComment());
        assertTrue(createdSettings.isEnableCommentComment());
        assertTrue(createdSettings.isEnableSendEmailMessage());

        Settings savedSettings = settingsRepository.findByUserId(currentAuthUserId).orElseThrow();
        assertEquals(currentAuthUserId, savedSettings.getUserId());
        assertTrue(savedSettings.isPost());
        assertTrue(savedSettings.isMessage());
        assertTrue(savedSettings.isFriendBirthday());
        assertFalse(savedSettings.isFriendRequest());
        assertTrue(savedSettings.isPostComment());
        assertTrue(savedSettings.isCommentComment());
        assertTrue(savedSettings.isSendEmailMessage());
    }

    @Test
    @DisplayName("testing create settings, but it already assigned to this user, expected error")
    void testCreateSettings_alreadyExists_error() {
        Long currentAuthUserId = 1L;
        SettingsDto settingsDto = saveSettingsInDb(currentAuthUserId, getTestSettingsDto());
        assertThrows(SettingsAlreadyExistsException.class, () -> {
            notificationService.createSettings(settingsDto, currentAuthUserId);
        });
    }

    @Test
    @DisplayName("testing update settings")
    void testUpdateSettings_correct_success() {
        Long currentAuthUserId = 1L;
        saveSettingsInDb(currentAuthUserId, getTestSettingsDto());
        notificationService.updateSettings(new SettingRq(false, NotificationType.POST), currentAuthUserId);
        assertFalse(settingsRepository.findByUserId(currentAuthUserId).get().isPost());
    }

    @Test
    @DisplayName("testing create new notification")
    void testCreateNotification() {
        NotificationInputDto notificationInputDto = NotificationInputDto.builder()
                .notificationType(NotificationType.MESSAGE)
                .content("content")
                .userId(1L)
                .authorId(1L)
                .build();
        notificationService.createNotification(notificationInputDto);
        Notification notification = notificationRepository.findAll().get(0);
        assertEquals(1, notificationRepository.findAll().size());
        assertEquals(NotificationStatus.SENT, notification.getNotificationStatus());
        assertEquals("content", notification.getContent());
    }

    @Test
    @DisplayName("testing get notifications")
    void testGetNotifications_correct_success() {
        Long currentAuthUserId = 1L;

        Notification notification = new Notification();
        notification.setUserId(currentAuthUserId);
        notification.setContent("Test notification");
        notification.setNotificationType(NotificationType.MESSAGE);
        notification.setNotificationStatus(NotificationStatus.SENT);
        notification.setTime(LocalDateTime.now());
        notificationRepository.save(notification);

        NotificationSentDto notifications = notificationService.getNotifications(currentAuthUserId);

        assertNotNull(notifications);
        assertEquals(1, notifications.getContent().size());
        assertEquals("Test notification", notifications.getContent().get(0).getData().getContent());
    }

    @Test
    @DisplayName("testing get notification count")
    void testGetCount_correct_success() {
        Long currentAuthUserId = 1L;

        Notification notification1 = new Notification();
        notification1.setUserId(currentAuthUserId);
        notification1.setContent("Test notification 1");
        notification1.setNotificationType(NotificationType.MESSAGE);
        notification1.setNotificationStatus(NotificationStatus.SENT);
        notification1.setTime(LocalDateTime.now());
        notificationRepository.save(notification1);

        Notification notification2 = new Notification();
        notification2.setUserId(currentAuthUserId);
        notification2.setContent("Test notification 2");
        notification2.setNotificationType(NotificationType.POST);
        notification2.setNotificationStatus(NotificationStatus.SENT);
        notification2.setTime(LocalDateTime.now());
        notificationRepository.save(notification2);

        NotificationCountRs notificationCount = notificationService.getCount(currentAuthUserId);

        assertNotNull(notificationCount);
        assertNotNull(notificationCount.getData());
        assertEquals(2, notificationCount.getData().getCount());
        assertNotNull(notificationCount.getTimestamp());
    }

    @Test
    @DisplayName("testing mark notifications as read")
    void testSetReaded_correct_success() {
        long currentAuthUserId = 1L;

        Notification notification = new Notification();
        notification.setUserId(currentAuthUserId);
        notification.setContent("Test notification");
        notification.setNotificationType(NotificationType.MESSAGE);
        notification.setNotificationStatus(NotificationStatus.SENT);
        notification.setTime(LocalDateTime.now());
        notificationRepository.save(notification);

        notificationService.setReaded(currentAuthUserId);

        Notification updatedNotification = notificationRepository.findById(notification.getId()).orElseThrow();
        assertEquals(NotificationStatus.READ, updatedNotification.getNotificationStatus());
    }

    @NonNull
    private SettingsDto saveSettingsInDb(Long currentAuthUserId, SettingsDto settingsDto) {
        Settings settings = settingsMapperV1.toEntity(currentAuthUserId, settingsDto);
        settingsRepository.save(settings);
        return settingsDto;
    }
}