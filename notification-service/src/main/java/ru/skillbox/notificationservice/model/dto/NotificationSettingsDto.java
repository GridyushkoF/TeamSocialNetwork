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
    private boolean enablePost;

    @JsonProperty
    @Schema(description = "Enable notifications for comments on posts")
    private boolean enablePostComment;

    @JsonProperty
    @Schema(description = "Enable notifications for comments on comments")
    private boolean enableCommentComment;

    @JsonProperty
    @Schema(description = "Enable notifications for friend requests")
    private boolean enableFriendRequest;

    @JsonProperty
    @Schema(description = "Enable notifications for new messages")
    private boolean enableMessage;

    @JsonProperty
    @Schema(description = "Enable notifications for friend birthdays")
    private boolean enableFriendBirthday;

    @JsonProperty
    @Schema(description = "Enable sending notification emails")
    private boolean enableSendEmailMessage;
}
