package ru.skillbox.adminservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.adminservice.service.AdminService;
import ru.skillbox.commonlib.dto.statistics.AdminStatisticsDto;
import ru.skillbox.commonlib.dto.statistics.PeriodRequestDto;
import ru.skillbox.commonlib.dto.statistics.UsersStatisticsDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.apiPrefix}" + "/admin-console/statistic")
public class AdminStatisticsController {
    private final AdminService adminService;

    @GetMapping("/post")
    public ResponseEntity<AdminStatisticsDto> getPostsStatistics(
            @ModelAttribute PeriodRequestDto periodRequestDto,
            HttpServletRequest request) {
        System.out.println(periodRequestDto);
        return ResponseEntity.ok(adminService.getPostsStatistics(periodRequestDto,request));
    }
    @GetMapping("/comment")
    public ResponseEntity<AdminStatisticsDto> getCommentsStatistics(
            @ModelAttribute PeriodRequestDto periodRequestDto,
            HttpServletRequest request) {
        return ResponseEntity.ok(adminService.getCommentsStatistics(periodRequestDto,request));
    }
    @GetMapping("/account")
    public ResponseEntity<UsersStatisticsDto> getUsersStatistics
            (@ModelAttribute PeriodRequestDto periodRequestDto,
             HttpServletRequest request
            ) {
        UsersStatisticsDto usersStatisticsDto = adminService.getUsersStatistics(periodRequestDto,request);
        return ResponseEntity.ok(usersStatisticsDto);
    }
    @GetMapping("/like")
    public ResponseEntity<AdminStatisticsDto> getLikesStatistics(
            @ModelAttribute PeriodRequestDto periodRequestDto,
            HttpServletRequest request) {
        return ResponseEntity.ok(adminService.getLikesStatistics(periodRequestDto,request));
    }
}
