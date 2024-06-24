package ru.skillbox.commondto;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.skillbox.userservice.model.dto.StatusCode;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class AccountDto {
    long id;
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

    public static AccountDto of(User user) {
        return new AccountDto()
                .setId(user.getId())
                .setEmail(user.getEmail())
                .setPhone(user.getPhone())
                .setAbout(user.getAbout())
                .setCity(user.getCity())
                .setCountry(user.getCountry())
                .setStatusCode(StatusCode.NONE)
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setRegDate(user.getRegDate())
                .setBirthDate(user.getBirthDate())
                .setMessagePermission(user.getMessagePermission())
                .setLastOnlineTime(user.getLastOnlineTime())
                .setOnline(user.isOnline())
                .setBlocked(user.isBlocked())
                .setDeleted(user.isDeleted())
                .setPhotoId(user.getPhotoId())
                .setPhotoName(user.getPhotoName())
                //.setRole(user.getRoles()) ??
                .setCreatedOn(user.getCreatedOn())
                .setUpdatedOn(user.getUpdatedOn())
                .setPassword(user.getPassword());

    }
}


