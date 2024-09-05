package ru.skillbox.userservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;
import ru.skillbox.commonlib.dto.account.StatusCode;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FriendShortDto {
    private Long id;
    private Long friendId;
    private boolean isDeleted;
    private StatusCode statusCode;
    private StatusCode previousStatusCode;
    private Integer rating;
}
