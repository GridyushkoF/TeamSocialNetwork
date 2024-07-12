package ru.skillbox.userservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.userservice.service.FriendshipService;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipService friendshipService;

    @PostMapping("/{id}/request")
    @ResponseStatus(HttpStatus.OK)
    public void requestFriendship(
            @PathVariable("id") Long accountId,
            HttpServletRequest request) {
        {
            Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
            friendshipService.requestFriendship(currentAuthUserId, accountId);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFriendship(
            @PathVariable("id") Long accountId,
            HttpServletRequest request) {
        {
            Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
            friendshipService.deleteFriendship(currentAuthUserId, accountId);
        }
    }

}
