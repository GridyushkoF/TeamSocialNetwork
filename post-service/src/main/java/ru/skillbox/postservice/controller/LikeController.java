package ru.skillbox.postservice.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.commonlib.dto.post.LikeDto;
import ru.skillbox.postservice.service.LikeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.apiPrefix}/post")
@SecurityRequirement(name = "bearerAuth")
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/{id}/like")
    @ResponseStatus(HttpStatus.CREATED)
    public void likePost(
            @PathVariable("id") Long postId,
            @RequestBody LikeDto likeDto,
            @RequestHeader("id") Long currentAuthUserId) {
        likeService.likePost(postId, likeDto, currentAuthUserId);
    }

    @DeleteMapping("/{id}/like")
    @ResponseStatus(HttpStatus.OK)
    public void unLikePost(
            @PathVariable("id") Long postId,
            @RequestHeader("id") Long currentAuthUserId) {
        likeService.unlikePost(postId, currentAuthUserId);
    }

    @PostMapping("/{id}/comment/{commentId}/like")
    @ResponseStatus(HttpStatus.CREATED)
    public void likeComment(
            @PathVariable("id") Long postId,
            @PathVariable("commentId") Long commentId,
            @RequestHeader("id") Long currentAuthUserId) {
        likeService.likeComment(postId, commentId, currentAuthUserId);
    }

    @DeleteMapping("/{id}/comment/{commentId}/like")
    @ResponseStatus(HttpStatus.OK)
    public void unLikeComment(
            @PathVariable("id") Long postId,
            @PathVariable("commentId") Long commentId,
            @RequestHeader("id") Long currentAuthUserId) {
        likeService.unlikeComment(postId, commentId, currentAuthUserId);
    }
}
