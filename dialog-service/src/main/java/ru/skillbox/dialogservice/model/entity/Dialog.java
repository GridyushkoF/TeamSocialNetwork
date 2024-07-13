package ru.skillbox.dialogservice.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Dialog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "unread_count")
    private Long unreadCount;
    @Column(name = "member1_id")
    private Long member1Id;
    @Column(name = "member2_id")
    private Long member2Id;
}
