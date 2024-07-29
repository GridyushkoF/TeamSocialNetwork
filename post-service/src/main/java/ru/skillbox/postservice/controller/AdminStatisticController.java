package ru.skillbox.postservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.commondto.dto.statistics.AdminStatisticsDto;
import ru.skillbox.commondto.dto.statistics.PeriodRequestDto;
import ru.skillbox.commondto.util.admin.AdminAccessUtil;
import ru.skillbox.postservice.service.admin.AdminStatisticsService;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.apiPrefix}" + "/post" + "/admin-api")
public class AdminStatisticController {
    private final AdminStatisticsService adminStatisticsService;

    @PostMapping("/get-comments-statistics")
    public ResponseEntity<AdminStatisticsDto> getCommentsAmountByPeriod(
            @RequestBody PeriodRequestDto periodRequestDto,
            HttpServletRequest request) {
        AdminAccessUtil.throwExceptionIfTokenNotAdmin(request);
        return ResponseEntity.ok(adminStatisticsService.getCommentsStatistics(periodRequestDto));
    }

    @PostMapping("/get-posts-statistics")
    public ResponseEntity<AdminStatisticsDto> getPostsAmountByPeriod(
            @RequestBody PeriodRequestDto periodRequestDto,
            HttpServletRequest request) {
        AdminAccessUtil.throwExceptionIfTokenNotAdmin(request);
        return ResponseEntity.ok(adminStatisticsService.getPostsStatistics(periodRequestDto));
    }

    @PostMapping("/get-likes-statistics")
    public ResponseEntity<AdminStatisticsDto> getLikesAmountByPeriod(
            @RequestBody PeriodRequestDto periodRequestDto,
            HttpServletRequest request) {
        AdminAccessUtil.throwExceptionIfTokenNotAdmin(request);
        return ResponseEntity.ok(adminStatisticsService.getLikesStatistics(periodRequestDto));
    }

}
