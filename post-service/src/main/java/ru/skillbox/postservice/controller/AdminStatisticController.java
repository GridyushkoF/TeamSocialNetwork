package ru.skillbox.postservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin Statistic Controller", description = "Admin Statistic API")
public class AdminStatisticController {
    private final AdminStatisticsService adminStatisticsService;

    @PostMapping("/statistic/comment")
    @Operation(summary = "Get comment statistics")
    public ResponseEntity<AdminStatisticsDto> getCommentStatistics(
            @RequestBody PeriodRequestDto periodRequestDto,
            HttpServletRequest request) {
        AdminAccessUtil.throwExceptionIfTokenNotAdmin(request);
        return ResponseEntity.ok(adminStatisticsService.getCommentsStatistics(periodRequestDto));
    }

    @PostMapping("/statistic/post")
    @Operation(summary = "Get posts statistics")
    public ResponseEntity<AdminStatisticsDto> getPostsStatistics(
            @RequestBody PeriodRequestDto periodRequestDto,
            HttpServletRequest request) {
        AdminAccessUtil.throwExceptionIfTokenNotAdmin(request);
        return ResponseEntity.ok(adminStatisticsService.getPostsStatistics(periodRequestDto));
    }

    @PostMapping("/statistic/like")
    @Operation(summary = "Get likes statistics")
    public ResponseEntity<AdminStatisticsDto> getLikesStatistics(
            @RequestBody PeriodRequestDto periodRequestDto,
            HttpServletRequest request) {
        AdminAccessUtil.throwExceptionIfTokenNotAdmin(request);
        return ResponseEntity.ok(adminStatisticsService.getLikesStatistics(periodRequestDto));
    }

}
