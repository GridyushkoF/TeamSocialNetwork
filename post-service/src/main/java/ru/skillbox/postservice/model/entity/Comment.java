package ru.skillbox.postservice.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "comment_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CommentType commentType;

    @CreationTimestamp
    private LocalDateTime time;

    @UpdateTimestamp
    @Column(name = "time_changed")
    private LocalDateTime timeChanged;

    @Column(name = "author_id")
    private Long authorId;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "comment_text", columnDefinition = "TEXT")
    private String commentText;

    @Column (name = "post_id")
    private Long postId;

    @Column(name = "is_blocked")
    private boolean isBlocked;

    @Column(name = "is_delete")
    private boolean isDelete;

    @Column(name = "image_path")
    private String imagePath;

    @OneToMany
    @JoinTable(
            name = "comments_likes",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "like_id")
    )
    private Set<Like> likes = Set.of();
}
