package ru.skillbox.userservice.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

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
    @Length(min = 8, message = "Минимальная длина пароля 8 символов!")
    String password;
}
