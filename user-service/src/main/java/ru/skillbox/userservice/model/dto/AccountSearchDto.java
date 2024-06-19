package ru.skillbox.userservice.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AccountSearchDto {
    List<Integer> ids;
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
