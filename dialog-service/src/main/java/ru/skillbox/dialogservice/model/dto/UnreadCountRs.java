package ru.skillbox.dialogservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UnreadCountRs {
    private String description;
    private String error;
    private Long timestamp;
    private UnreadCountDto data;
    @JsonProperty(namespace = "error_description")
    private String errorDescription;
}
