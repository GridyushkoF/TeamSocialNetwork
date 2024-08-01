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
    public ResponseEntity<AdminStatisticsDto> getPostsAmountByPeriod(
            @ModelAttribute PeriodRequestDto periodRequestDto,
            HttpServletRequest request) {
        System.out.println(periodRequestDto);
        return ResponseEntity.ok(adminService.getPostsAmountByPeriod(periodRequestDto,request));
    }
    @GetMapping("/comment")
    public ResponseEntity<AdminStatisticsDto> getCommentsAmountByPeriod(
            @ModelAttribute PeriodRequestDto periodRequestDto,
            HttpServletRequest request) {
        return ResponseEntity.ok(adminService.getCommentsAmountByPeriod(periodRequestDto,request));
    }
    @GetMapping("/account")
    public ResponseEntity<UsersStatisticsDto> getRegisteredUsersAmountByPeriod
            (@ModelAttribute PeriodRequestDto periodRequestDto,
             HttpServletRequest request
            ) {
        UsersStatisticsDto usersStatisticsDto = adminService.getUsersAmountByPeriod(periodRequestDto,request);
        return ResponseEntity.ok(usersStatisticsDto);
    }
    @GetMapping("/like")
    public ResponseEntity<AdminStatisticsDto> getLikesAmountByPeriod(
            @ModelAttribute PeriodRequestDto periodRequestDto,
            HttpServletRequest request) {
        return ResponseEntity.ok(adminService.getLikesAmountByPeriod(periodRequestDto,request));
    }
}
