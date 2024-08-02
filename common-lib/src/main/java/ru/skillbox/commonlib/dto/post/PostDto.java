package ru.skillbox.commonlib.dto.post;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.commonlib.dto.post.wrappers.TagWrapper;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

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
    private boolean isBlocked = false;
    private boolean isDelete = false;
    private Long commentsCount;
    private List<TagWrapper> tags;
    private Long likeAmount;
    private boolean myLike;
    private String imagePath;
    private ZonedDateTime publishDate;
}
