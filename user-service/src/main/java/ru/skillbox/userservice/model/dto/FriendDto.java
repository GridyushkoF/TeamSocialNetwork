package ru.skillbox.userservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.skillbox.commonlib.dto.account.StatusCode;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO for a single friend")
public class FriendDto {
    @Schema(description = "Friend's user ID")
    private Long friendId;

    @Schema(description = "Friend's profile photo URL")
    private String photo;

    @Schema(description = "Friendship status code")
    private StatusCode statusCode;

    @Schema(description = "Friend's first name")
    private String firstName;

    @Schema(description = "Friend's last name")
    private String lastName;

    @Schema(description = "Friend's city")
    private String city;

    @Schema(description = "Friend's country")
    private String country;

    @Schema(description = "Friend's birth date")
    private LocalDateTime birthDate;

    @Schema(description = "Indicates whether the friend is currently online")
    private Boolean isOnline;
}
