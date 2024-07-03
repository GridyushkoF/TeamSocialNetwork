package ru.skillbox.dialogservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetMessagesRs {
    private String error;
    private String errorDescription;
    private Long timestamp;
    private int total;
    private int offset;
    private int perPage;
    private List<MessageShortDto> data;
}
