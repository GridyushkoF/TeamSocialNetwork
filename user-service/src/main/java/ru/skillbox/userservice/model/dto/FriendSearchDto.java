package ru.skillbox.userservice.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.skillbox.commondto.account.StatusCode;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class FriendSearchDto {
    private List<Long> ids;
    private String firstName;
    private LocalDateTime birthDateFrom;
    private LocalDateTime birthDateTo;
    private String city;
    private String country;
    private Integer ageTo;
    private Integer ageFrom;
    private StatusCode statusCode;
}
