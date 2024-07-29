package ru.skillbox.commondto.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.commondto.dto.statistics.DateCountPointDto;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminStatisticsDto {
    private Long count;
    private List<DateCountPointDto> countPerMonth;
    private List<DateCountPointDto> countPerHours;
}