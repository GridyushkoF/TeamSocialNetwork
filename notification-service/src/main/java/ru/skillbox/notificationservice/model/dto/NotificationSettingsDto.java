package ru.skillbox.notificationservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationSettingsDto {
    @JsonProperty
    private boolean enablePost;
    @JsonProperty
    private boolean enablePostComment;
    @JsonProperty
    private boolean enableCommentComment;
    @JsonProperty
    private boolean enableFriendRequest;
    @JsonProperty
    private boolean enableMessage;
    @JsonProperty
    private boolean enableFriendBirthday;
    @JsonProperty
    private boolean enableSendEmailMessage;
}
