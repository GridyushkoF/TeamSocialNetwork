package ru.skillbox.postservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.commonlib.dto.statistics.AdminStatisticsDto;
import ru.skillbox.commonlib.dto.statistics.PeriodRequestDto;
import ru.skillbox.commonlib.util.admin.AdminAccessUtil;
import ru.skillbox.postservice.service.admin.AdminStatisticsService;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.apiPrefix}" + "/post")
public class AdminStatisticController {
    private final AdminStatisticsService adminStatisticsService;

    @PostMapping("/statistic/comment")
    public ResponseEntity<AdminStatisticsDto> getCommentStatistics(
            @RequestBody PeriodRequestDto periodRequestDto,
            HttpServletRequest request) {
        AdminAccessUtil.throwExceptionIfTokenNotAdmin(request);
        return ResponseEntity.ok(adminStatisticsService.getCommentsStatistics(periodRequestDto));
    }

    @PostMapping("/statistic/post")
    public ResponseEntity<AdminStatisticsDto> getPostsStatistics(
            @RequestBody PeriodRequestDto periodRequestDto,
            HttpServletRequest request) {
        AdminAccessUtil.throwExceptionIfTokenNotAdmin(request);
        return ResponseEntity.ok(adminStatisticsService.getPostsStatistics(periodRequestDto));
    }

    @PostMapping("/statistic/like")
    public ResponseEntity<AdminStatisticsDto> getLikesStatistics(
            @RequestBody PeriodRequestDto periodRequestDto,
            HttpServletRequest request) {
        AdminAccessUtil.throwExceptionIfTokenNotAdmin(request);
        return ResponseEntity.ok(adminStatisticsService.getLikesStatistics(periodRequestDto));
    }

}
