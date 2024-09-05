package ru.skillbox.notificationservice.model.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "settings")
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Builder.Default
    @Column(name = "friend_request")
    private boolean friendRequest = true;
    @Builder.Default
    @Column(name = "friend_birthday")
    private boolean friendBirthday = true;
    @Builder.Default
    @Column(name = "post_comment")
    private boolean postComment = true;
    @Column(name = "comment_comment")
    @Builder.Default
    private boolean commentComment = true;
    @Column(name = "post")
    @Builder.Default
    private boolean post = true;
    @Column(name = "message")
    @Builder.Default
    private boolean message = true;
    @Column(name = "send_email_message")
    @Builder.Default
    private boolean sendEmailMessage = true;
}
