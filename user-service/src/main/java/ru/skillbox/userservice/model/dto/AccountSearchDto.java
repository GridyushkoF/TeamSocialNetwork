package ru.skillbox.userservice.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class AccountSearchDto {
    List<Long> ids;
    String author;
    String firstName;
    String lastName;
    LocalDateTime birthDateFrom;
    LocalDateTime birthDateTo;
    String city;
    String country;
    boolean isBlocked;
    boolean isDeleted;
    int ageTo;
    int ageFrom;
}
