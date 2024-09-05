package ru.skillbox.notificationservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@Schema(description = "DTO for sent notification response")
public class NotificationSentDto {

    @JsonProperty
    @Schema(description = "Timestamp of the notification")
    private LocalDateTime timeStamp;

    @JsonProperty
    @Schema(description = "List of notification data")
    private List<NotificationDataRs> content;
}
