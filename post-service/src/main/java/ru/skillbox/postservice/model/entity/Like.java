package ru.skillbox.postservice.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "post_id", columnDefinition = "BIGINT NOT NULL")
    private long postId;

    @Column(name = "comment_id", columnDefinition = "BIGINT")
    private long commentId;
}
