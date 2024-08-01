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
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminStatisticsService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
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
                "publishDate");
    }
    public AdminStatisticsDto getStatistics(PeriodRequestDto periodRequestDto,
                                            String entityName,
                                            String dateFieldName) {
        LocalDateTime fromDate = periodRequestDto.getFirstMonth().toLocalDateTime();
        LocalDateTime toDate = periodRequestDto.getLastMonth().toLocalDateTime();
        Long postsAmountByPeriod = getAmount(fromDate, toDate,entityName);
        List<DateCountPointDto> monthlyPostsStatistics = adminStatisticsRepository.getDateCountStatistics(
                dateFieldName, "MONTH", entityName, fromDate, toDate
        );
        List<DateCountPointDto> hourlyPostsStatistics = adminStatisticsRepository.getDateCountStatistics(
                dateFieldName, "HOUR", entityName, fromDate, toDate
        );
        return new AdminStatisticsDto(postsAmountByPeriod, monthlyPostsStatistics, hourlyPostsStatistics);
    }

    private Long getAmount(LocalDateTime fromDate, LocalDateTime toDate,String entityName) {
        Integer postsAmountByPeriod = null;
        switch (entityName) {
            case "Like" -> postsAmountByPeriod = likeRepository.countByCreationDateTimeBetween(fromDate, toDate);
            case "Post" -> postsAmountByPeriod = postRepository.countByPublishDateBetween(fromDate,toDate);
            case "Comment" -> postsAmountByPeriod = commentRepository.countByTimeBetween(fromDate,toDate);
        }
        return Long.parseLong(postsAmountByPeriod.toString());
    }
}
