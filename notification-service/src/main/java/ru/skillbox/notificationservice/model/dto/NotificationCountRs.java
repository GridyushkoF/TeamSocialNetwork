package ru.skillbox.notificationservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationCountRs {
    @JsonProperty
    private Instant timestamp;
    @JsonProperty
    private CountRs data;
}
