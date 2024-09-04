package ru.skillbox.commonlib.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    Integer ageTo;
    Integer ageFrom;
}
