package ru.skillbox.notificationservice.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import ru.skillbox.notificationservice.model.enums.NotificationStatus;
import ru.skillbox.notificationservice.model.enums.NotificationType;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreationTimestamp
    private LocalDateTime time;
    @Column(name = "author_id")
    private Long authorId;
    @Column(name = "user_id")
    private Long userId;
    private String content;
    @Column(name = "notification_type")
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
    @Column(name = "Notification_status")
    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;
}
