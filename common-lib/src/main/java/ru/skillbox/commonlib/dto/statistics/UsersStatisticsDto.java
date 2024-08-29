package ru.skillbox.commonlib.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UsersStatisticsDto {
    private long count;
    private List<AgeCountDto> countPerAge;
    private List<DateCountPointDto> countPerMonth;
}
