package ru.skillbox.notificationservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class NotificationSentDto {

    @JsonProperty
    private LocalDateTime timeStamp;
    @JsonProperty
    private List<NotificationDataRs> content;

}
