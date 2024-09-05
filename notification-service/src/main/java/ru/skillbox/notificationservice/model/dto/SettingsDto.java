package ru.skillbox.notificationservice.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO representing user settings")
public class SettingsDto {

    @Schema(description = "ID of the settings")
    private Long id;

    @Schema(description = "ID of the user")
    private Long userId;

    @Schema(description = "Enable notifications for friend requests")
    private boolean friendRequest;

    @Schema(description = "Enable notifications for friend birthdays")
    private boolean friendBirthday;

    @Schema(description = "Enable notifications for comments on posts")
    private boolean postComment;

    @Schema(description = "Enable notifications for comments on comments")
    private boolean commentComment;

    @Schema(description = "Enable notifications for new posts")
    private boolean post;

    @Schema(description = "Enable notifications for new messages")
    private boolean message;

    @Schema(description = "Enable sending notification phone messages")
    private boolean sendPhoneMessage;

    @Schema(description = "Enable sending notification emails")
    private boolean sendEmailMessage;

}
