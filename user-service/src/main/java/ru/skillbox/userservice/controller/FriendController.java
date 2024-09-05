package ru.skillbox.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.Map;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Friend Controller", description = "Friend API")
public class FriendController {

    private final FriendshipService friendshipService;

    @PostMapping("/{id}/request")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add friend request")
    public void addFriend(
            @PathVariable("id") Long accountId,
            @RequestHeader("id") Long currentAuthUserId) {
        friendshipService.requestFriendship(currentAuthUserId, accountId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete friend by id")
    public void deleteFriendship(
            @PathVariable("id") Long accountId,
            @RequestHeader("id") Long currentAuthUserId) {
        friendshipService.deleteFriendship(currentAuthUserId, accountId);
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "Approve friend request")
    public void approveFriend(
            @PathVariable("id") Long accountId,
            @RequestHeader("id") Long currentAuthUserId) {
        friendshipService.approveFriendship(currentAuthUserId, accountId);
    }

    @PutMapping("/block/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Block friend by id")
    public void blockFriend(
            @PathVariable("id") Long accountId,
            @RequestHeader("id") Long currentAuthUserId) {
        friendshipService.blockAccount(currentAuthUserId, accountId);
    }

    @PutMapping("/unblock/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Unblock friend by id")
    public void unblockFriend(
            @PathVariable("id") Long accountId,
            @RequestHeader("id") Long currentAuthUserId) {
        deleteFriendship(accountId, currentAuthUserId);
    }

    @PostMapping("/subscribe/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Subscribe by id")
    public void subscribe(
            @PathVariable("id") Long accountId,
            @RequestHeader("id") Long currentAuthUserId) {
        friendshipService.subscribeToAccount(currentAuthUserId, accountId);
    }

    @GetMapping
    @Operation(summary = "Get friends")
    public ResponseEntity<Page<FriendDto>> getFriends(
            @RequestParam StatusCode statusCode,
            @RequestParam(defaultValue = "3") int size,
            @RequestHeader("id") Long currentAuthUserId) {
        return ResponseEntity.ok(friendshipService.getFriendsByStatus(statusCode, size, currentAuthUserId));
    }

    @GetMapping("/recommendations")
    @Operation(summary = "Get by recommendation")
    public ResponseEntity<List<RecommendedFriendDto>> getByRecommendation(
            @RequestHeader("id") Long currentAuthUserId) {
        return ResponseEntity.ok(friendshipService.getFriendRecommendations(currentAuthUserId));
    }

    @GetMapping("/recommendations?")
    public ResponseEntity<List<RecommendedFriendDto>> getByRecommendationWithQuestionMark(
            @RequestHeader("id") Long currentAuthUserId) {
        return getByRecommendation(currentAuthUserId);
    }

    @GetMapping("/count")
    @Operation(summary = "Count request")
    public ResponseEntity<CountDto> requestCount(
            @RequestHeader("id") Long currentAuthUserId) {
        return ResponseEntity.ok(new CountDto(friendshipService.getFriendRequestCount(currentAuthUserId)));
    }
}
