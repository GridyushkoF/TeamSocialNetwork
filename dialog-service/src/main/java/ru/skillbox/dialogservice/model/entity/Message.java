package ru.skillbox.dialogservice.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import ru.skillbox.dialogservice.model.dto.MessageStatus;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    private Long time;
    @Column(name = "author_id")
    private Long authorId;
    @Column(name = "recipient_id")
    private Long recipientId;
    @Column(name = "message_text")
    private String messageText;
    @Enumerated(EnumType.STRING)
    private MessageStatus status;
}
