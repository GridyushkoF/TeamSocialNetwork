package ru.skillbox.dialogservice.model.dto;

import lombok.Data;
import ru.skillbox.dialogservice.model.enums.MessageType;

@Data
public class ConversationMessageDto {

    private MessageType type;

    private Long recipientId;

    private MessageDto data;

}
