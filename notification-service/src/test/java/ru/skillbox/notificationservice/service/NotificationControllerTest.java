package ru.skillbox.notificationservice.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.web.resources.static-locations=",
        "spring.mvc.static-path-pattern=/**"
})
@AutoConfigureMockMvc
public class NotificationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private SettingsRepository settingsRepository;

    @MockBean
    private SettingsMapperV1 settingsMapper;

    @MockBean
    private NotificationRepository notificationRepository;

    @MockBean
    private NotificationMapperV1 notificationMapper;

    @Test
    void getNotificationSetting() throws Exception {
        Long userId = 1L;
        NotificationSettingsDto expectedDto = new NotificationSettingsDto();
        when(notificationService.getSettings(any(HttpServletRequest.class))).thenReturn(expectedDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/notifications/settings")
                        .header("id", userId.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedDto)));

        verify(notificationService).getSettings(any(HttpServletRequest.class));
    }

    @Test
    void updateNotificationSettings() throws Exception {
        SettingRq settingRq = new SettingRq(true, NotificationType.POST);
        NotificationSettingsDto expectedDto = new NotificationSettingsDto();

        expectedDto.setEnablePost(true);
        expectedDto.setEnablePostComment(true);
        expectedDto.setEnableCommentComment(true);
        expectedDto.setEnableFriendRequest(true);
        expectedDto.setEnableMessage(true);
        expectedDto.setEnableFriendBirthday(true);
        expectedDto.setEnableSendEmailMessage(true);

        when(notificationService.updateSettings(eq(settingRq), any(HttpServletRequest.class)))
                .thenReturn(expectedDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/notifications/settings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(settingRq)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedDto)));

        verify(notificationService).updateSettings(eq(settingRq), any(HttpServletRequest.class));
    }
    @Test
    void createNotificationSetting() throws Exception {
        SettingsDto settingsDto = new SettingsDto();
        NotificationSettingsDto expectedSettingsDto = new NotificationSettingsDto();

        when(notificationService.createSettings(any(SettingsDto.class), any(HttpServletRequest.class)))
                .thenReturn(expectedSettingsDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/notifications/settings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(settingsDto)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedSettingsDto)));

        ArgumentCaptor<SettingsDto> settingsCaptor = ArgumentCaptor.forClass(SettingsDto.class);
        verify(notificationService).createSettings(settingsCaptor.capture(), any(HttpServletRequest.class));
    }
    @Test
    void getNotification() throws Exception {
        Long userId = 1L;
        List<Notification> notifications = Collections.singletonList(new Notification());
        NotificationSentDto expectedResponse = new NotificationSentDto();

        when(notificationService.getNotifications(any(HttpServletRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/notifications")
                .header("id", userId.toString())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedResponse)));

        verify(notificationService).getNotifications(any(HttpServletRequest.class));
    }

    @Test
    void createNotification() throws Exception {
        NotificationInputDto notification = NotificationInputDto.builder()
                .authorId(1L)
                .userId(2L)
                .notificationType(NotificationType.FRIEND_REQUEST)
                .content("Test notification")
                .build();

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notification)))
                .andExpect(status().isCreated());
    }
    @Test
    void getNotificationCount() throws Exception {
        Long userId = 1L;
        List<Notification> notifications = Collections.singletonList(new Notification());
        NotificationCountRs expectedResponse = new NotificationCountRs();

        when(notificationService.getCount(any(HttpServletRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/notifications/count")
                        .header("id", userId.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedResponse)));

        verify(notificationService).getCount(any(HttpServletRequest.class));
    }

    @Test
    void setReaded() throws Exception {
        long userId = 1L;
        List<Notification> notifications = Arrays.asList(
                new Notification(),
                new Notification()
        );

        // Мокирование репозитория
        when(notificationRepository.findAllByUserIdAndNotificationStatus(userId, NotificationStatus.SENT))
                .thenReturn(notifications);
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/notifications/readed")
                        .header("id", String.valueOf(userId)).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(notificationRepository, times(1)).findAllByUserIdAndNotificationStatus(userId, NotificationStatus.SENT);
        verify(notificationRepository, times(2)).save(any(Notification.class));
    }
}
