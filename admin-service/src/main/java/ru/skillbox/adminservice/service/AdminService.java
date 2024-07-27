package ru.skillbox.adminservice.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import ru.skillbox.commondto.PeriodRequestDto;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final WebClient webClient;

    public Integer getPostsAmountByPeriod(
            PeriodRequestDto periodRequestDto,
            HttpServletRequest request) {
        return getCountByPeriod(periodRequestDto, request, "/api/v1/post/admin-api/get-comments-amount");
    }

    public Integer getCommentsAmountByPeriod(
            PeriodRequestDto periodRequestDto,
            HttpServletRequest request) {
        return getCountByPeriod(periodRequestDto, request, "/api/v1/post/admin-api/get-posts-amount");
    }

    public Integer getUsersAmountByPeriod(PeriodRequestDto periodRequestDto,
                                          HttpServletRequest request) {
        return getCountByPeriod(periodRequestDto, request, "/api/v1/account/admin-api/get-registered-users-amount");
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

    public Integer getCountByPeriod(PeriodRequestDto periodRequestDto,
                                    HttpServletRequest request,
                                    String relativePath) {
        WebClient.RequestHeadersSpec<?> spec = webClient.post()
                .uri(relativePath)
                .body(BodyInserters.fromValue(periodRequestDto));

        spec = spec.header("Authorization", request.getHeader("Authorization"));
        return spec.retrieve()
                .bodyToMono(Integer.class)
                .block();
    }
}
