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
    @Builder.Default
    private boolean enablePost = true;
    @JsonProperty
    @Builder.Default
    private boolean enablePostComment = true;
    @JsonProperty
    @Builder.Default
    private boolean enableCommentComment = true;
    @JsonProperty
    @Builder.Default
    private boolean enableFriendRequest = true;
    @JsonProperty
    @Builder.Default
    private boolean enableMessage = true;
    @JsonProperty
    @Builder.Default
    private boolean enableFriendBirthday = true;
    @JsonProperty
    @Builder.Default
    private boolean enableSendEmailMessage = true;
}
