package ru.skillbox.notificationservice.mapper.V1;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.skillbox.commondto.notification.NotificationStatus;
import ru.skillbox.notificationservice.model.dto.*;
import ru.skillbox.notificationservice.model.entity.Notification;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapperV1 {

    NotificationDto toDto(Notification notification);

    default NotificationDto toNotificationDto(Notification notification) {
        NotificationDto dto = toDto(notification);
        dto.setIsStatusSent(notification.getNotificationStatus() == NotificationStatus.SENT);
        return dto;
    }

    Notification toEntity(NotificationInputDto dto);

    default NotificationSentDto toResponse(List<Notification> list) {

        NotificationSentDto response = new NotificationSentDto();
        List<NotificationDto> dtoList = list.stream().map(this::toNotificationDto).toList();
        List<NotificationDataRs> dataList = new ArrayList<>();
        for (NotificationDto currentDto : dtoList) {
            NotificationDataRs currentData = new NotificationDataRs();
            currentData.setData(currentDto);
            dataList.add(currentData);
        }
        response.setContent(dataList);
        response.setTimeStamp(LocalDateTime.now());

        return response;
    }

    default NotificationCountRs toCountResponse(int count) {
        CountRs countRs =
                new CountRs(count);
        NotificationCountRs response = new NotificationCountRs();
        response.setTimestamp(Instant.now());
        response.setData(countRs);

        return response;
    }
}
