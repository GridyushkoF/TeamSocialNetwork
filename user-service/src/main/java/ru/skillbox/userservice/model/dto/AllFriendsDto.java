package ru.skillbox.userservice.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.skillbox.commonlib.dto.account.StatusCode;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class AllFriendsDto {
    private Long id;
    private String photo;
    private StatusCode statusCode;
    private String firstName;
    private String lastName;
    private String city;
    private String country;
    private LocalDateTime birthDate;
    private Boolean isOnline;
}
