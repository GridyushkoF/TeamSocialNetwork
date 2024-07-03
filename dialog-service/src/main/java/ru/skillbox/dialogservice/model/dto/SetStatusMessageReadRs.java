package ru.skillbox.dialogservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SetStatusMessageReadRs {
    private String error;
    private Long timestamp;
    private SetStatusMessageReadDto data;
    @JsonProperty(namespace = "error_description")
    private String errorDescription;
}
