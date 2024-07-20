package ru.skillbox.userservice.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.skillbox.commondto.account.StatusCode;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class FriendDto {
    private Long friendId;
    private String photo;
    private StatusCode statusCode;
    private String firstName;
    private String lastName;
    private String city;
    private String country;
    private LocalDateTime birthDate;
    private Boolean isOnline;
}
