package ru.skillbox.commonlib.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class AccountDto {
    private Long id;
    private String email;
    private String phone;
    private String photo;
    private String about;
    private String city;
    private String country;
    private String token;
    private StatusCode statusCode;
    private String firstName;
    private String lastName;
    private ZonedDateTime regDate;
    private ZonedDateTime birthDate;
    private String messagePermission;
    private ZonedDateTime lastOnlineTime;
    @JsonProperty("isOnline")
    private boolean isOnline;
    @JsonProperty("isBlocked")
    private boolean isBlocked;
    @JsonProperty("isDeleted")
    private boolean isDeleted;
    private String photoId;
    private String photoName;
    private Role role;
    private ZonedDateTime createdOn;
    private ZonedDateTime updatedOn;
    private String password;
    private String emojiStatus;
    private String profileCover;
}
