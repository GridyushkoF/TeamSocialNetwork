package ru.skillbox.commonlib.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReactionDto {
    private LikeReactionType reactionType;
    private Long count;
}
