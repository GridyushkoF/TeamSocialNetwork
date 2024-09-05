package ru.skillbox.userservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.skillbox.commonlib.dto.account.StatusCode;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@Schema(description = "DTO for searching friends")
public class FriendSearchDto {

    @Schema(description = "List of user IDs to filter by")
    private List<Long> ids;

    @Schema(description = "Filter by first name")
    private String firstName;

    @Schema(description = "Filter by birth date (from)")
    private LocalDateTime birthDateFrom;

    @Schema(description = "Filter by birth date (to)")
    private LocalDateTime birthDateTo;

    @Schema(description = "Filter by city")
    private String city;

    @Schema(description = "Filter by country")
    private String country;

    @Schema(description = "Filter by maximum age")
    private Integer ageTo;

    @Schema(description = "Filter by minimum age")
    private Integer ageFrom;

    @Schema(description = "Filter by friendship status code")
    private StatusCode statusCode;
}
