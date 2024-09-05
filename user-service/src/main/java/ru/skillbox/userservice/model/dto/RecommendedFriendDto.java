package ru.skillbox.userservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.commonlib.dto.account.StatusCode;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO for a recommended friend")
public class RecommendedFriendDto {

    @Schema(description = "User ID")
    private Long id;

    @Schema(description = "Indicates whether the user is deleted")
    private boolean isDeleted;

    @Schema(description = "Friendship status code")
    private StatusCode statusCode;

    @Schema(description = "Friend's user ID")
    private Long friendId;

    @Schema(description = "Recommendation rating")
    private int rating;
}
