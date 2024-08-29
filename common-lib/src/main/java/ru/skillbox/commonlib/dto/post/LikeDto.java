package ru.skillbox.commonlib.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LikeDto {
    private String type;
    @JsonProperty("reactionType")
    private LikeReactionType reactionType;
}
