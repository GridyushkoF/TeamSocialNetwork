package ru.skillbox.dialogservice.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import ru.skillbox.dialogservice.model.enums.MessageStatus;

import java.time.Instant;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "dialog_id", nullable = false)
    private Long dialogId;
    @CreationTimestamp
    private Instant time;
    @Column(name = "author_id")
    private Long authorId;
    @Column(name = "recipient_id")
    private Long recipientId;
    @Column(name = "message_text")
    private String messageText;
    @Enumerated(EnumType.STRING)
    private MessageStatus status;
}
