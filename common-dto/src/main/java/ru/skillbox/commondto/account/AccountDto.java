package ru.skillbox.commondto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
