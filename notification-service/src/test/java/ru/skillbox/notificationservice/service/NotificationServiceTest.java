package ru.skillbox.notificationservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import ru.skillbox.commondto.notification.NotificationStatus;
import ru.skillbox.commondto.notification.NotificationType;
import ru.skillbox.notificationservice.mapper.V1.NotificationMapperV1;
import ru.skillbox.notificationservice.mapper.V1.SettingsMapperV1;
import ru.skillbox.notificationservice.model.dto.*;
import ru.skillbox.notificationservice.model.entity.Notification;
import ru.skillbox.notificationservice.model.entity.Settings;
import ru.skillbox.notificationservice.repository.NotificationRepository;
import ru.skillbox.notificationservice.repository.SettingsRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private  SettingsRepository settingsRepository;
    @Mock
    private  NotificationRepository notificationRepository;
    @Mock
    private  SettingsMapperV1 settingsMapper;
    @Mock
    private  NotificationMapperV1 notificationMapper;
    @InjectMocks
    private NotificationService notificationService;

    @Test
    void getSettings() {
        Long userId = 1L;
        Settings newSettings = new Settings();
        newSettings.setUserId(userId);
        NotificationSettingsDto expectedDto = new NotificationSettingsDto();

        when(settingsRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(settingsRepository.saveAndFlush(any(Settings.class))).thenReturn(newSettings);
        when(settingsMapper.toDto(newSettings)).thenReturn(expectedDto);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("id", userId.toString());
        NotificationSettingsDto resultDto = notificationService.getSettings(request);

        assertEquals(expectedDto, resultDto);
        verify(settingsRepository).findByUserId(userId);
        verify(settingsRepository).saveAndFlush(any(Settings.class));
        verify(settingsMapper).toDto(newSettings);
        verifyNoMoreInteractions(settingsRepository, settingsMapper);
    }

    @Test
    void createSettings() {
        Long userId = 1L;
        SettingsDto settingsDto = new SettingsDto();
        Settings newSettingsEntity = new Settings();
        newSettingsEntity.setUserId(userId);
        NotificationSettingsDto expectedDto = new NotificationSettingsDto();

        when(settingsRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(settingsMapper.toEntity(userId, settingsDto)).thenReturn(newSettingsEntity);
        when(settingsRepository.saveAndFlush(any(Settings.class))).thenReturn(newSettingsEntity);
        when(settingsRepository.save(newSettingsEntity)).thenReturn(newSettingsEntity);
        when(settingsMapper.toDto(newSettingsEntity)).thenReturn(expectedDto);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("id", userId.toString());
        NotificationSettingsDto resultDto = notificationService.createSettings(settingsDto, request);

        assertEquals(expectedDto, resultDto);
        verify(settingsRepository).findByUserId(userId);
        verify(settingsMapper).toEntity(userId, settingsDto);
        verify(settingsRepository).saveAndFlush(any(Settings.class));
        verify(settingsRepository).save(newSettingsEntity);
        verify(settingsMapper).toDto(newSettingsEntity);
        verifyNoMoreInteractions(settingsRepository, settingsMapper);
    }

    @Test
    void updateSettings() {
        Long userId = 1L;
        SettingRq settingRq = new SettingRq(true, NotificationType.POST);
        Settings existingSettings = new Settings();
        existingSettings.setUserId(userId);
        existingSettings.setPost(false);

        NotificationSettingsDto expectedDto = NotificationSettingsDto.builder()
                .enablePost(true)
                .enablePostComment(false)
                .enableCommentComment(false)
                .enableFriendRequest(false)
                .enableMessage(false)
                .enableFriendBirthday(false)
                .enableSendEmailMessage(false)
                .build();

        when(settingsRepository.findByUserId(userId)).thenReturn(Optional.of(existingSettings));
        when(settingsRepository.save(existingSettings)).thenReturn(existingSettings);
        when(settingsMapper.toDto(existingSettings)).thenReturn(expectedDto);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("id", userId.toString());
        NotificationSettingsDto resultDto = notificationService.updateSettings(settingRq, request);

        assertEquals(expectedDto, resultDto);
        verify(settingsRepository).findByUserId(userId);
        verify(settingsRepository).save(existingSettings);
        verify(settingsMapper).toDto(existingSettings);
        verifyNoMoreInteractions(settingsRepository, settingsMapper);
    }

    @Test
    void createNotification() {
        NotificationInputDto notificationInputDto = NotificationInputDto.builder()
                .authorId(1L)
                .userId(2L)
                .notificationType(NotificationType.POST)
                .content("Sample Notification Content")
                .build();

        Notification notificationEntity = new Notification();
        NotificationDto expectedNotificationDto = new NotificationDto();

        when(notificationMapper.toEntity(notificationInputDto)).thenReturn(notificationEntity);
        when(notificationRepository.save(notificationEntity)).thenReturn(notificationEntity);
        when(notificationMapper.toNotificationDto(notificationEntity)).thenReturn(expectedNotificationDto);

        NotificationDto resultNotificationDto = notificationService.createNotification(notificationInputDto);

        assertEquals(expectedNotificationDto, resultNotificationDto);
        verify(notificationMapper).toEntity(notificationInputDto);
        verify(notificationRepository).save(notificationEntity);
        verify(notificationMapper).toNotificationDto(notificationEntity);
        verifyNoMoreInteractions(notificationRepository, notificationMapper);
    }


    @Test
    void getNotifications() {
        Long userId = 1L;
        List<Notification> notifications = Collections.singletonList(new Notification());
        NotificationSentDto expectedResponse = new NotificationSentDto();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("id", String.valueOf(userId));
        when(notificationRepository.findAllByUserIdAndNotificationStatus(userId, NotificationStatus.SENT))
                .thenReturn(notifications);
        when(notificationMapper.toResponse(notifications)).thenReturn(expectedResponse);

        NotificationSentDto resultResponse = notificationService.getNotifications(request);

        assertEquals(expectedResponse, resultResponse);
        verify(notificationRepository).findAllByUserIdAndNotificationStatus(userId, NotificationStatus.SENT);
        verify(notificationMapper).toResponse(notifications);
        verifyNoMoreInteractions(notificationRepository, notificationMapper);
    }


    @Test
    void getCount() {
        Long userId = 1L;
        List<Notification> notifications = Collections.singletonList(new Notification());
        NotificationCountRs expectedResponse = new NotificationCountRs();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("id", String.valueOf(userId));
        when(notificationRepository.findAllByUserIdAndNotificationStatus(userId, NotificationStatus.SENT))
                .thenReturn(notifications);
        when(notificationMapper.toCountResponse(notifications.size())).thenReturn(expectedResponse);

        NotificationCountRs resultResponse = notificationService.getCount(request);

        assertEquals(expectedResponse, resultResponse);
        verify(notificationRepository).findAllByUserIdAndNotificationStatus(userId, NotificationStatus.SENT);
        verify(notificationMapper).toCountResponse(notifications.size());
        verifyNoMoreInteractions(notificationRepository, notificationMapper);
    }

    @Test
    void setReaded() {
        Long userId = 1L;
        List<Notification> notifications = Arrays.asList(
                new Notification(),
                new Notification()
        );

        when(notificationRepository.findAllByUserIdAndNotificationStatus(userId, NotificationStatus.SENT))
                .thenReturn(notifications);
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("id", String.valueOf(userId));
        notificationService.setReaded(request);

        verify(notificationRepository, times(1)).findAllByUserIdAndNotificationStatus(userId, NotificationStatus.SENT);
        verify(notificationRepository, times(2)).save(any(Notification.class));

    }
}