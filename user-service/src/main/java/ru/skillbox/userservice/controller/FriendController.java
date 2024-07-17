package ru.skillbox.userservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.userservice.model.dto.AllFriendsDto;
import ru.skillbox.userservice.model.dto.FriendSearchDto;
import ru.skillbox.userservice.model.dto.RecommendationFriendsDto;
import ru.skillbox.userservice.service.FriendshipService;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
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
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> approveFriend(@PathVariable("id") Long accountId, HttpServletRequest request) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        friendshipService.approveFriendship(currentAuthUserId, accountId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/block/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> blockFriend(@PathVariable("id") Long accountId, HttpServletRequest request) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        friendshipService.blockFriend(currentAuthUserId, accountId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/subscribe/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> subscribe(@PathVariable("id") Long accountId, HttpServletRequest request) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        friendshipService.subscribeToFriend(currentAuthUserId, accountId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<AllFriendsDto> getFriends(@Valid FriendSearchDto friendSearchDto, HttpServletRequest request) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        //TODO: Сделать по-нормальному получение друзей
        return ResponseEntity.ok(new AllFriendsDto());
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Void> getFriendByAccountDto(@PathVariable("accountId") Long accountId, HttpServletRequest request) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        friendshipService.getFriendById(currentAuthUserId, accountId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/recommendations")
    public ResponseEntity<RecommendationFriendsDto> getByRecommendation(HttpServletRequest request) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        //TODO: Сделать нормальное получение друзей по рекомендации
        return ResponseEntity.ok(new RecommendationFriendsDto());
    }

    @GetMapping("/friendId")
    public ResponseEntity<Void> getFriendId(HttpServletRequest request) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        friendshipService.getAllFriends(currentAuthUserId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Void> requestCount(HttpServletRequest request) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        friendshipService.getFriendRequestCount(currentAuthUserId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/blockFriendId")
    public ResponseEntity<Void> getBlockFriendId(HttpServletRequest request) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        friendshipService.getBlockedFriendIds(currentAuthUserId);
        return ResponseEntity.ok().build();
    }
}
