package ru.skillbox.userservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Schema(description = "DTO for recommended friends")
public class RecommendationFriendsDto {
    @Schema(description = "User ID")
    private Long id;

    @Schema(description = "User's profile photo URL")
    private String photo;

    @Schema(description = "User's first name")
    private String firstName;

    @Schema(description = "User's last name")
    private String lastName;
}
