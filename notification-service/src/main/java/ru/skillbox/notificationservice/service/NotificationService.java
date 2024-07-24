package ru.skillbox.notificationservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.skillbox.commondto.account.AccountDto;
import ru.skillbox.notificationservice.model.dto.NotificationCreateDto;
import ru.skillbox.notificationservice.model.dto.NotificationSettingDto;
import ru.skillbox.notificationservice.model.entity.Notification;
import ru.skillbox.notificationservice.model.entity.NotificationCreate;
import ru.skillbox.notificationservice.model.entity.NotificationResponse;
import ru.skillbox.notificationservice.repository.NotificationCreateRepository;
import ru.skillbox.notificationservice.util.settings.NotificationSetting;
import ru.skillbox.notificationservice.util.settings.NotificationSettingResponse;
import ru.skillbox.notificationservice.model.entity.NotificationType;
import ru.skillbox.notificationservice.repository.NotificationRepository;
import ru.skillbox.notificationservice.util.settings.NotificationSettingData;
import ru.skillbox.notificationservice.util.BeanUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final NotificationRepository notificationRepository;
    private final NotificationCreateRepository notificationCreateRepository;
    private final NotificationTypeHandler notificationTypeHandler;


    public NotificationSettingResponse getSettings(Long id) {

        NotificationSetting notificationSetting = notificationRepository.findByUserId(id).get();

        NotificationSettingResponse response = new NotificationSettingResponse();
        response.setTime(LocalDateTime.now());
        response.setUserId(notificationSetting.getUserId());
        response.setData((ArrayList<NotificationSettingData>) createNotificationSettingData(notificationSetting));
        return response;

    }

    private List<NotificationSettingData> createNotificationSettingData(NotificationSetting notificationSetting) {
        List<NotificationSettingData> data = new ArrayList<>();
        for (NotificationType type : NotificationType.values()) {
            if (notificationTypeHandler.isEnabled(notificationSetting, type)) {
                data.add(new NotificationSettingData(true, type));
            }
        }
        return data;
    }

    public void createSettings(NotificationSettingDto dto) {

        NotificationSetting notificationSetting = new NotificationSetting();
        notificationSetting.setId(dto.getId());
        notificationSetting.setUserId(dto.getUserId());

        BeanUtil.copyPropertiesFromDto(dto, notificationSetting);

        notificationRepository.save(notificationSetting);
}

    public void updateSettings(NotificationSettingData notificationSettingData , Long id){

        NotificationSetting notificationSetting = notificationRepository.findByUserId(id).get();

        switch (notificationSettingData.getNotificationType()) {
            case POST:
                notificationSetting.setPost(notificationSettingData.isEnable());
                break;
            case MESSAGE:
                notificationSetting.setMessage(notificationSettingData.isEnable());
                break;
            case FRIEND_BIRTHDAY:
                notificationSetting.setFriendBirthday(notificationSettingData.isEnable());
                break;
            case FRIEND_REQUEST:
                notificationSetting.setFriendRequest(notificationSettingData.isEnable());
                break;
            case POST_COMMENT:
                notificationSetting.setPostComment(notificationSettingData.isEnable());
                break;
            case COMMENT_COMMENT:
                notificationSetting.setCommentComment(notificationSettingData.isEnable());
                break;
            case SEND_EMAIL_MESSAGE:
                notificationSetting.setSendEmailMessage(notificationSettingData.isEnable());
                break;
            case SEND_PHONE_MESSAGE:
                notificationSetting.setSendPhoneMessage(notificationSettingData.isEnable());
                break;
        }

        notificationRepository.save(notificationSetting);
    }

    public void createNotification(NotificationCreateDto dto) {
        NotificationCreate notificationCreate = new NotificationCreate();
        notificationCreate.setId(dto.getId());
        notificationCreate.setAuthorId(dto.getAuthorId());
        notificationCreate.setUserId(dto.getUserId());

        BeanUtil.copyPropertiesFromCreatDto(dto, notificationCreate);

        notificationCreateRepository.save(notificationCreate);
    }

    public NotificationResponse getNotification(HttpServletRequest request) throws Exception {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        AccountDto userDto = getAccountDtoByIdFromUserService(currentAuthUserId,request);

        NotificationResponse notificationResponse = new NotificationResponse();
        notificationResponse.setTimeStamp(LocalDateTime.now());

        ArrayList<Notification> data = new ArrayList<>();
        data.add(new Notification(0L,userDto, "content" , NotificationType.FRIEND_REQUEST, LocalDateTime.now()));

        notificationResponse.setData(data);
        return notificationResponse;

    }

    private AccountDto getAccountDtoByIdFromUserService(Long accountId, HttpServletRequest request) throws Exception {
        String url = "http://localhost:9090/api/v1/account/{accountId}";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",request.getHeader("Authorization"));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class,
                accountId
        );
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.getBody(), AccountDto.class);
    }
}
