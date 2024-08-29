package ru.skillbox.notificationservice.model.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettingsDto {
    private Long id;
    private Long userId;
    private boolean friendRequest;
    private boolean friendBirthday;
    private boolean postComment;
    private boolean commentComment;
    private boolean post;
    private boolean message;
    private boolean sendPhoneMessage;
    private boolean sendEmailMessage;
}
