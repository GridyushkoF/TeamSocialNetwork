package ru.skillbox.dialogservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;
import ru.skillbox.dialogservice.model.enums.MessageStatus;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MessageDto {
    private Long id;
    private Long dialogId;
    private Instant time;
    @JsonProperty("conversationPartner1")
    private Long authorId;
    @JsonProperty("conversationPartner2")
    private Long recipientId;
    private String messageText;
    private MessageStatus status;

}
