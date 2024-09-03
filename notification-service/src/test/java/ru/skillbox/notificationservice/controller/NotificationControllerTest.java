package ru.skillbox.notificationservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.skillbox.commonlib.event.notification.NotificationStatus;
import ru.skillbox.commonlib.event.notification.NotificationType;
import ru.skillbox.notificationservice.model.dto.*;
import ru.skillbox.notificationservice.model.entity.Notification;
import ru.skillbox.notificationservice.repository.NotificationRepository;
import ru.skillbox.notificationservice.service.NotificationService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class NotificationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private NotificationRepository notificationRepository;

    @Test
    void getNotificationSetting() throws Exception {
        long userId = 1L;
        NotificationSettingsDto expectedDto = new NotificationSettingsDto();
        when(notificationService.getSettings(any(Long.class))).thenReturn(expectedDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/notifications/settings")
                        .header("id", Long.toString(userId))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedDto)));

        verify(notificationService).getSettings(any(Long.class));
    }

    @Test
    void createNotificationSetting() throws Exception {
        SettingsDto settingsDto = new SettingsDto();
        NotificationSettingsDto expectedSettingsDto = new NotificationSettingsDto();

        when(notificationService.createSettings(any(SettingsDto.class), any(Long.class)))
                .thenReturn(expectedSettingsDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/notifications/settings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("id", Long.toString(1L))
                        .content(objectMapper.writeValueAsString(settingsDto)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedSettingsDto)));

        ArgumentCaptor<SettingsDto> settingsCaptor = ArgumentCaptor.forClass(SettingsDto.class);
        verify(notificationService).createSettings(settingsCaptor.capture(), any(Long.class));
    }

    @Test
    void getNotification() throws Exception {
        Long userId = 1L;
        NotificationSentDto expectedResponse = new NotificationSentDto();
        when(notificationService.getNotifications(any(Long.class))).thenReturn(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/notifications")
                .header("id", userId.toString())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        verify(notificationService).getNotifications(any(Long.class));
    }

    @Test
    void createNotification() throws Exception {
        NotificationInputDto notification = NotificationInputDto.builder()
                .authorId(1L)
                .userId(2L)
                .notificationType(NotificationType.FRIEND_REQUEST)
                .content("Test notification")
                .build();

        mockMvc.perform(post("/api/v1/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notification)))
                .andExpect(status().isCreated());
    }

    @Test
    void getNotificationCount() throws Exception {
        long userId = 1L;
        NotificationCountRs expectedResponse = new NotificationCountRs();

        when(notificationService.getCount(any(Long.class))).thenReturn(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/notifications/count")
                        .header("id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedResponse)));

        verify(notificationService).getCount(any(Long.class));
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

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/notifications/readed")
                        .header("id", String.valueOf(userId)).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
