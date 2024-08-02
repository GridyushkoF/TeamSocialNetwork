package ru.skillbox.commonlib.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    private CommentType commentType;
    private ZonedDateTime time;
    private ZonedDateTime timeChanged;
    private Long authorId;
    private Long parentId;
    private String commentText;
    private Long postId;
    private boolean isBlocked;
    private boolean isDelete;
    private Long likeAmount;
    private boolean myLike;
    private Long commentsCount;
    private String imagePath;
}
