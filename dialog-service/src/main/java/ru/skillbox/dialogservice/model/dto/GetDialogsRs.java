package ru.skillbox.dialogservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetDialogsRs {
    private String error;
    @JsonProperty(namespace = "error_description")
    private String errorDescription;
    private Long timestamp;
    private int total;
    private int offset;
    private int perPage;
    private Long currentUserId;
    private List<DialogDto> data;
}
