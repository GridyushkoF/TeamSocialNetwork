package ru.skillbox.dialogservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MessageShortDto {
    private Long id;
    private Long authorId;
    private Long time;
    private String messageText;
}
