package ru.skillbox.notificationservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "DTO for notification data response")
public class NotificationDataRs {
    @JsonProperty
    @Schema(description = "Notification data")
    private NotificationDto data;
}
