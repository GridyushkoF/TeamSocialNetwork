package ru.skillbox.userservice.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.commonlib.dto.account.StatusCode;
import ru.skillbox.commonlib.dto.statistics.CountDto;
import ru.skillbox.userservice.model.dto.FriendDto;
import ru.skillbox.userservice.model.dto.RecommendedFriendDto;
import ru.skillbox.userservice.service.FriendshipService;

import java.util.List;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class FriendController {

    private final FriendshipService friendshipService;

    @PostMapping("/{id}/request")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable("id") Long accountId, HttpServletRequest request) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        friendshipService.requestFriendship(currentAuthUserId, accountId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFriendship(@PathVariable("id") Long accountId, HttpServletRequest request) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        friendshipService.deleteFriendship(currentAuthUserId, accountId);
    }

    @PutMapping("/{id}/approve")
    public void approveFriend(@PathVariable("id") Long accountId, HttpServletRequest request) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        friendshipService.approveFriendship(currentAuthUserId, accountId);
    }

    @PutMapping("/block/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void blockFriend(@PathVariable("id") Long accountId, HttpServletRequest request) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        friendshipService.blockAccount(currentAuthUserId, accountId);
    }

    @PutMapping("/unblock/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void unblockFriend(@PathVariable("id") Long accountId, HttpServletRequest request) {
        deleteFriendship(accountId, request);
    }

    @PostMapping("/subscribe/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void subscribe(@PathVariable("id") Long accountId, HttpServletRequest request) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        friendshipService.subscribeToAccount(currentAuthUserId, accountId);
    }

    @GetMapping
    public ResponseEntity<Page<FriendDto>> getFriends(
            @RequestParam StatusCode statusCode,
            @RequestParam(defaultValue = "3") int size,
            HttpServletRequest request) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        return ResponseEntity.ok(friendshipService.getFriendsByStatus(statusCode, size, currentAuthUserId));
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<RecommendedFriendDto>> getByRecommendation(HttpServletRequest request) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        return ResponseEntity.ok(friendshipService.getFriendRecommendations(currentAuthUserId));
    }

    @GetMapping("/count")
    public ResponseEntity<CountDto> requestCount(HttpServletRequest request) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        return ResponseEntity.ok(new CountDto(friendshipService.getFriendRequestCount(currentAuthUserId)));
    }
}
