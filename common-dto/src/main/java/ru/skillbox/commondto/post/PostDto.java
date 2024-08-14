package ru.skillbox.commondto.post;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.commondto.post.wrappers.TagWrapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostDto {
    private Long id;
    private LocalDateTime time;
    private LocalDateTime timeChanged;
    private Long authorId;
    private String title;
    private PostType type;
    private String postText;
    private boolean isBlocked;
    private boolean isDelete;
    private Long commentsCount;
    private List<TagWrapper> tags;
    private Long likeAmount;
    private boolean myLike;
    private String imagePath;
    private LocalDateTime publishDate;
}