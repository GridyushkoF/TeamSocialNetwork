package ru.skillbox.adminservice.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import ru.skillbox.commonlib.dto.statistics.PeriodRequestDto;
import ru.skillbox.commonlib.dto.statistics.AdminStatisticsDto;
import ru.skillbox.commonlib.dto.statistics.UsersStatisticsDto;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final WebClient webClient;

    public AdminStatisticsDto getPostsStatistics(
            PeriodRequestDto periodRequestDto,
            HttpServletRequest request) {
        return getStatisticsByPeriod(periodRequestDto, request, "/api/v1/post/statistic/post",AdminStatisticsDto.class);
    }

    public AdminStatisticsDto getCommentsStatistics(
            PeriodRequestDto periodRequestDto,
            HttpServletRequest request) {
        return getStatisticsByPeriod(periodRequestDto, request, "/api/v1/post/statistic/comment",AdminStatisticsDto.class);
    }

    public UsersStatisticsDto getUsersStatistics(PeriodRequestDto periodRequestDto,
                                                 HttpServletRequest request) {
        return getStatisticsByPeriod(periodRequestDto, request, "/api/v1/account/statistic", UsersStatisticsDto.class);
    }
    public AdminStatisticsDto getLikesStatistics(PeriodRequestDto periodRequestDto,
                                                 HttpServletRequest request) {
        return getStatisticsByPeriod(periodRequestDto,request,"/api/v1/post/statistic/like",AdminStatisticsDto.class);
    }

    public void blockOrUnblockUser(
            Long userId,
            boolean shouldBlock,
            HttpServletRequest request) {
        String path = "/api/v1/account/admin-api/block/" + userId;
        WebClient.RequestHeadersSpec<?> query;
        if (shouldBlock) {
            query = webClient.put()
                    .uri(path);
        } else {
            query = webClient.delete()
                    .uri(path);
        }

        query = query.header("Authorization", request.getHeader("Authorization"));
        query.retrieve();
    }

    public <T> T getStatisticsByPeriod(PeriodRequestDto periodRequestDto,
                                                    HttpServletRequest request,
                                                    String relativePath,
                                                    Class<T> resultDtoClass) {
        WebClient.RequestHeadersSpec<?> query = webClient.post()
                .uri(relativePath)
                .body(BodyInserters.fromValue(periodRequestDto));

        query = query.header("Authorization", request.getHeader("Authorization"));
        return query.retrieve()
                .bodyToMono(resultDtoClass)
                .block();
    }
}
