package ru.skillbox.userservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.commonlib.dto.account.StatusCode;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecommendedFriendDto {
    private Long id;
    private boolean isDeleted;
    private StatusCode statusCode;
    private Long friendId;
    private int rating;
}
