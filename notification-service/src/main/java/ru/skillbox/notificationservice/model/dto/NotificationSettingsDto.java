package ru.skillbox.notificationservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO for notification settings")
public class NotificationSettingsDto {
    @JsonProperty
    @Schema(description = "Enable notifications for new posts")
    @Builder.Default
    private boolean enablePost = true;
    @JsonProperty
    @Schema(description = "Enable notifications for comments on posts")
    @Builder.Default
    private boolean enablePostComment = true;
    @JsonProperty
    @Schema(description = "Enable notifications for comments on comments")
    @Builder.Default
    private boolean enableCommentComment = true;
    @JsonProperty
    @Schema(description = "Enable notifications for friend requests")
    @Builder.Default
    private boolean enableFriendRequest = true;
    @JsonProperty
    @Schema(description = "Enable notifications for new messages")
    @Builder.Default
    private boolean enableMessage = true;
    @JsonProperty
    @Schema(description = "Enable notifications for friend birthdays")
    @Builder.Default
    private boolean enableFriendBirthday = true;
    @JsonProperty
    @Schema(description = "Enable sending notification emails")
    @Builder.Default
    private boolean enableSendEmailMessage = true;
}
