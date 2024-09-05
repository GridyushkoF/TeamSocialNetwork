package ru.skillbox.notificationservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO for notification count response")
public class NotificationCountRs {
    @JsonProperty
    @Schema(description = "Timestamp of the last update")
    private Instant timestamp;

    @JsonProperty
    @Schema(description = "Count data")
    private CountRs data;
}
