package ru.skillbox.dialogservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MessageDto {
    private Long id;
    private Long dialogId;
    private Long time;
    private Long authorId;
    private Long recipientId;
    private String messageText;
    private MessageStatus status;

}
