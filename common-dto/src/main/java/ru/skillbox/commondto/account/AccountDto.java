package ru.skillbox.commondto.account;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@Builder
public class AccountDto {
    Long id;
    String email;
    String phone;
    String photo;
    String about;
    String city;
    String country;
    String token;
    StatusCode statusCode;
    String firstName;
    String lastName;
    LocalDateTime regDate;
    LocalDateTime birthDate;
    String messagePermission;
    LocalDateTime lastOnlineTime;
    boolean isOnline;
    boolean isBlocked;
    boolean isDeleted;
    String photoId;
    String photoName;
    Role role;
    LocalDateTime createdOn;
    LocalDateTime updatedOn;
    String password;
}
