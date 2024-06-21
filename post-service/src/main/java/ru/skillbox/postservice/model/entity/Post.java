package ru.skillbox.postservice.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    private LocalDateTime time;

    @UpdateTimestamp
    @Column(name = "time_changed")
    private LocalDateTime timeChanged;

    @Column(name = "author_id")
    private Long authorId;

    private String title;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "post_text", columnDefinition = "TEXT")
    private String postText;

    @Column(name = "is_blocked")
    private boolean isBlocked;

    @Column(name = "is_delete")
    private boolean isDelete;

    @ElementCollection
    @CollectionTable(name = "post_tags", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "tag")
    private List<String> tags;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "publish_date")
    private LocalDateTime publishDate;

    @Column(name = "comments_count")
    private Long commentsCount;

    @Column(name = "like_amount")
    private Long likeAmount;
    @OneToMany
    private Set<Like> likes;
    private boolean myLike;
}