package ru.skillbox.notificationservice.util.settings;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notification_settings")
public class NotificationSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
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
