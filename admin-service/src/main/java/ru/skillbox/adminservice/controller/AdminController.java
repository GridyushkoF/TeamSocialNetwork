package ru.skillbox.adminservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.adminservice.service.AdminService;
import ru.skillbox.commondto.PeriodRequestDto;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.apiPrefix}" + "/admin")
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/get-posts-amount")
    public ResponseEntity<Map<Object, Object>> getPostsAmountByPeriod(
            @RequestBody PeriodRequestDto periodRequestDto,
            HttpServletRequest request) {
        return ResponseEntity.ok(Map.of("posts_amount",
                adminService.getPostsAmountByPeriod(periodRequestDto,request)));
    }
    @PostMapping("/get-comments-amount")
    public ResponseEntity<Map<Object, Object>> getCommentsAmountByPeriod(
            @RequestBody PeriodRequestDto periodRequestDto,
            HttpServletRequest request) {
        return ResponseEntity.ok(Map.of("comments_amount",
                adminService.getCommentsAmountByPeriod(periodRequestDto,request)));
    }
    @PostMapping("/get-registered-users-amount")
    public ResponseEntity<Map<Object, Object>> getRegisteredUsersAmountByPeriod
            (@RequestBody PeriodRequestDto periodRequestDto,
             HttpServletRequest request
    ) {
        return ResponseEntity.ok(Map.of("users_amount",
                adminService.getUsersAmountByPeriod(periodRequestDto,request)));
    }
    @PostMapping("/block-user/{userId}")
    public ResponseEntity<Map<Object,Object>> blockUser(
            @PathVariable Long userId,
            HttpServletRequest request) {
        adminService.blockOrUnblockUser(userId,true,request);
        return ResponseEntity.ok(Map.of("message","user with id " + userId + " blocked successfully"));
    }
    @PostMapping("/unblock-user/{userId}")
    public ResponseEntity<Map<Object,Object>> unblock (
            @PathVariable Long userId,
            HttpServletRequest request) {
        adminService.blockOrUnblockUser(userId,false,request);
        return ResponseEntity.ok(Map.of("message","user with id " + userId + " unblocked successfully"));
    }
}
