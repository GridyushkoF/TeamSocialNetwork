package ru.skillbox.commonlib.dto.post;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReactionDto {
    private LikeReactionType reactionType;
    private Long count;
}