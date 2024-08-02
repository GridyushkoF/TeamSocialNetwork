package ru.skillbox.postservice.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.skillbox.commonlib.dto.post.PostType;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
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
    private PostType type;

    @Column(name = "post_text", columnDefinition = "TEXT")
    private String postText;

    @Column(name = "is_blocked")
    private boolean isBlocked;

    @Column(name = "is_delete")
    private boolean isDelete;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "post2tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;


    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "publish_date")
    private ZonedDateTime publishDate;

    @OneToMany
    @JoinTable(
            name = "posts_likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "like_id")
    )
    private Set<Like> likes;
}