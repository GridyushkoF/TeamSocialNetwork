package ru.skillbox.notificationservice.model.entity;


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
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "friend_request")
    private boolean friendRequest;
    @Column(name = "friend_birthday")
    private boolean friendBirthday;
    @Column(name = "post_comment")
    private boolean postComment;
    @Column(name = "comment_comment")
    private boolean commentComment;
    @Column(name = "post")
    private boolean post;
    @Column(name = "message")
    private boolean message;
    @Column(name = "send_email_message")
    private boolean sendEmailMessage;
}
