package ru.skillbox.adminservice.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import ru.skillbox.commondto.dto.statistics.PeriodRequestDto;
import ru.skillbox.commondto.dto.statistics.CountDto;
import ru.skillbox.commondto.dto.statistics.AdminStatisticsDto;
import ru.skillbox.commondto.dto.statistics.UsersStatisticsDto;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final WebClient webClient;

    public AdminStatisticsDto getPostsAmountByPeriod(
            PeriodRequestDto periodRequestDto,
            HttpServletRequest request) {
        return getStatisticsByPeriod(periodRequestDto, request, "/api/v1/post/admin-api/get-posts-statistics",AdminStatisticsDto.class);
    }

    public AdminStatisticsDto getCommentsAmountByPeriod(
            PeriodRequestDto periodRequestDto,
            HttpServletRequest request) {
        return getStatisticsByPeriod(periodRequestDto, request, "/api/v1/post/admin-api/get-comments-statistics",AdminStatisticsDto.class);
    }

    public UsersStatisticsDto getUsersAmountByPeriod(PeriodRequestDto periodRequestDto,
                                          HttpServletRequest request) {
        return getStatisticsByPeriod(periodRequestDto, request, "/api/v1/account/admin-api/get-registered-users-statistics", UsersStatisticsDto.class);
    }
    public AdminStatisticsDto getLikesAmountByPeriod(PeriodRequestDto periodRequestDto,
                                           HttpServletRequest request) {
        return getStatisticsByPeriod(periodRequestDto,request,"/api/v1/post/admin-api/get-likes-statistics",AdminStatisticsDto.class);
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
