package ru.skillbox.notificationservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NotificationDataRs {
    @JsonProperty
    private NotificationDto data;
}
