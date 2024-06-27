package ru.skillbox.commondto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.postservice.model.dto.PostType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private List<String> tags;
    private Long likeAmount;
    private boolean myLike;
    private String imagePath;
    private LocalDateTime publishDate;
}
