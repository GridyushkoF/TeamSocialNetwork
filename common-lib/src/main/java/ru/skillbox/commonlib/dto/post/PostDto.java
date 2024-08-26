package ru.skillbox.commonlib.dto.post;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.commonlib.dto.post.wrappers.TagWrapper;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostDto {
    private Long id;
    private ZonedDateTime time;
    private ZonedDateTime timeChanged;
    private Long authorId;
    private String title;
    private PostType type;
    private String postText;
    private boolean isBlocked = false;
    private boolean isDelete = false;
    private Long commentsCount;
    private List<TagWrapper> tags;
    private List<ReactionDto> reactions;
    private LikeReactionType myReaction;
    private Long likeAmount;
    private boolean myLike;
    private String imagePath;
    private ZonedDateTime publishDate;
}
