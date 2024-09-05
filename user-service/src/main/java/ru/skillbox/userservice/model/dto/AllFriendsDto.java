package ru.skillbox.userservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.skillbox.commonlib.dto.account.StatusCode;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@Schema(description = "DTO for all friends")
public class AllFriendsDto {
    @Schema(description = "User ID")
    private Long id;

    @Schema(description = "User's profile photo URL")
    private String photo;

    @Schema(description = "Friendship status code")
    private StatusCode statusCode;

    @Schema(description = "User's first name")
    private String firstName;

    @Schema(description = "User's last name")
    private String lastName;

    @Schema(description = "User's city")
    private String city;

    @Schema(description = "User's country")
    private String country;

    @Schema(description = "User's birth date")
    private LocalDateTime birthDate;

    @Schema(description = "Indicates whether the user is currently online")
    private Boolean isOnline;
}
