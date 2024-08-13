package ru.skillbox.notificationservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NotificationServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SettingsRepository settingsRepository;

    @MockBean
    private SettingsMapperV1 settingsMapper;

    @MockBean
    private NotificationRepository notificationRepository;

    @MockBean
    private NotificationMapperV1 notificationMapper;

    @Test
    void getSettings() throws Exception {
        Long userId = 1L;
        Settings newSettings = new Settings();
        newSettings.setUserId(userId);
        NotificationSettingsDto expectedDto = new NotificationSettingsDto();

        when(settingsRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(settingsRepository.saveAndFlush(any(Settings.class))).thenReturn(newSettings);
        when(settingsMapper.toDto(newSettings)).thenReturn(expectedDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/notifications/settings")
                        .header("id", userId.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedDto)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.enablePost").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.enablePostComment").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.enableCommentComment").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.enableFriendRequest").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.enableMessage").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.enableFriendBirthday").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.enableSendEmailMessage").value(false));

        verify(settingsRepository).findByUserId(userId);
        verify(settingsRepository).saveAndFlush(any(Settings.class));
        verify(settingsMapper).toDto(newSettings);
        verifyNoMoreInteractions(settingsRepository, settingsMapper);
    }
    @Test
    void createSettings() throws Exception {
        Long userId = 1L;
        SettingsDto settingsDto = new SettingsDto();

        when(settingsRepository.findByUserId(userId)).thenReturn(Optional.empty());

        when(settingsMapper.toEntity(userId, settingsDto))
                .thenReturn(new Settings() {{ setUserId(userId); }});

        when(settingsRepository.saveAndFlush(any(Settings.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(settingsRepository.save(any(Settings.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        NotificationSettingsDto expectedDto = new NotificationSettingsDto();
        when(settingsMapper.toDto(any(Settings.class))).thenReturn(expectedDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/notifications/settings")
                        .header("id", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(settingsDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedDto)));

        verify(settingsRepository).findByUserId(userId);
        verify(settingsMapper).toEntity(userId, settingsDto);
        verify(settingsRepository).saveAndFlush(any(Settings.class));
        verify(settingsRepository).save(any(Settings.class));
        verify(settingsMapper).toDto(any(Settings.class));
        verifyNoMoreInteractions(settingsRepository, settingsMapper);
    }

    @Test
    void updateSettings() throws Exception {
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

        mockMvc.perform(MockMvcRequestBuilders.put("/api/notifications/settings")
                        .header("id", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(settingRq)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedDto)));

        verify(settingsRepository).findByUserId(userId);
        verify(settingsRepository).save(existingSettings);
        verify(settingsMapper).toDto(existingSettings);
        verifyNoMoreInteractions(settingsRepository, settingsMapper);
    }

    @Test
    void createNotification() throws Exception {
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

        mockMvc.perform(MockMvcRequestBuilders.post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notificationInputDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedNotificationDto)));

        verify(notificationMapper).toEntity(notificationInputDto);
        verify(notificationRepository).save(notificationEntity);
        verify(notificationMapper).toNotificationDto(notificationEntity);
        verifyNoMoreInteractions(notificationRepository, notificationMapper);
    }
    @Test
    void getNotifications() throws Exception {
        Long userId = 1L;
        List<Notification> notifications = Collections.singletonList(new Notification());
        NotificationSentDto expectedResponse = new NotificationSentDto();

        when(notificationRepository.findAllByUserIdAndNotificationStatus(userId, NotificationStatus.SENT))
                .thenReturn(notifications);
        when(notificationMapper.toResponse(notifications)).thenReturn(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/notifications")
                        .header("id", String.valueOf(userId))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedResponse)));

        verify(notificationRepository).findAllByUserIdAndNotificationStatus(userId, NotificationStatus.SENT);
        verify(notificationMapper).toResponse(notifications);
        verifyNoMoreInteractions(notificationRepository, notificationMapper);
    }

    @Test
    void getCount() throws Exception {
        Long userId = 1L;
        List<Notification> notifications = Collections.singletonList(new Notification());
        NotificationCountRs expectedResponse = new NotificationCountRs();

        when(notificationRepository.findAllByUserIdAndNotificationStatus(userId, NotificationStatus.SENT))
                .thenReturn(notifications);
        when(notificationMapper.toCountResponse(notifications.size())).thenReturn(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/notifications/count")
                        .header("id", String.valueOf(userId))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedResponse)));

        verify(notificationRepository).findAllByUserIdAndNotificationStatus(userId, NotificationStatus.SENT);
        verify(notificationMapper).toCountResponse(notifications.size());
        verifyNoMoreInteractions(notificationRepository, notificationMapper);
    }

    @Test
    void setReaded() throws Exception {
        long userId = 1L;
        List<Notification> notifications = Arrays.asList(
                new Notification(),
                new Notification()
        );

        when(notificationRepository.findAllByUserIdAndNotificationStatus(userId, NotificationStatus.SENT))
                .thenReturn(notifications);
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/notifications/readed")
                        .header("id", String.valueOf(userId))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(notificationRepository, times(1)).findAllByUserIdAndNotificationStatus(userId, NotificationStatus.SENT);
        verify(notificationRepository, times(2)).save(any(Notification.class));
    }

}