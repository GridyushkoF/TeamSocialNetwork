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
public class DialogDto {
    private Long id;
    private int unreadCount;
    @JsonProperty("conversationPartner1")
    private Long member1Id;
    @JsonProperty("conversationPartner2")
    private Long member2Id;
}
