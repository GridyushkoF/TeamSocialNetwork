package ru.skillbox.commondto.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UsersStatisticsDto {
    private int count;
    private List<AgeCountDto> countPerAge;
    private List<DateCountPointDto> countPerMonth;
}
