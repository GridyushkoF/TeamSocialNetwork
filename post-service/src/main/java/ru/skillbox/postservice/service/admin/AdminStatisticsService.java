package ru.skillbox.postservice.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillbox.commonlib.dto.statistics.AdminStatisticsDto;
import ru.skillbox.commonlib.dto.statistics.DateCountPointDto;
import ru.skillbox.commonlib.dto.statistics.PeriodRequestDto;
import ru.skillbox.commonlib.util.admin.AdminStatisticsRepository;
import ru.skillbox.postservice.repository.CommentRepository;
import ru.skillbox.postservice.repository.LikeRepository;
import ru.skillbox.postservice.repository.PostRepository;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminStatisticsService {
    private final AdminStatisticsRepository adminStatisticsRepository;

    public AdminStatisticsDto getCommentsStatistics(PeriodRequestDto periodRequestDto) {
        return getStatistics(periodRequestDto,
                "Comment",
                "time");
    }

    public AdminStatisticsDto getLikesStatistics(PeriodRequestDto periodRequestDto) {
        return getStatistics(periodRequestDto,
                "Like",
                "creationDateTime");
    }

    public AdminStatisticsDto getPostsStatistics(PeriodRequestDto periodRequestDto) {
        return getStatistics(periodRequestDto,
                "Post",
                "time");
    }
    public AdminStatisticsDto getStatistics(PeriodRequestDto periodRequestDto,
                                            String entityName,
                                            String dateFieldName) {
        ZonedDateTime fromDate = periodRequestDto.getFirstMonth();
        ZonedDateTime toDate = periodRequestDto.getLastMonth();
        Long postsAmountByPeriod = adminStatisticsRepository.countEntities(entityName,dateFieldName,fromDate,toDate);
        List<DateCountPointDto> monthlyPostsStatistics = adminStatisticsRepository.getDateCountStatistics(
                dateFieldName, "MONTH", entityName, fromDate, toDate
        );
        List<DateCountPointDto> hourlyPostsStatistics = adminStatisticsRepository.getDateCountStatistics(
                dateFieldName, "HOUR", entityName, fromDate, toDate
        );
        return new AdminStatisticsDto(postsAmountByPeriod, monthlyPostsStatistics, hourlyPostsStatistics);
    }

}
