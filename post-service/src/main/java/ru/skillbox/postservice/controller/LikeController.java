package ru.skillbox.postservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.commondto.post.LikeDto;
import ru.skillbox.postservice.service.LikeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.apiPrefix}/post")
public class LikeController {
    private final LikeService likeService;
    @PostMapping("/{id}/like")
    @ResponseStatus(HttpStatus.CREATED)
    public void likePost(
            @PathVariable("id") Long postId,
            @RequestBody LikeDto likeDto,
            HttpServletRequest request
    ) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        likeService.likePost(postId,likeDto,currentAuthUserId);
    }

    @DeleteMapping("/{id}/like")
    @ResponseStatus(HttpStatus.OK)
    public void unLikePost(
            @PathVariable("id") Long postId,
            HttpServletRequest request
    ) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        likeService.unlikePost(postId,currentAuthUserId);
    }

    @PostMapping("/{id}/comment/{commentId}/like")
    @ResponseStatus(HttpStatus.CREATED)
    public void likeComment(
            @PathVariable("id") Long postId,
            @PathVariable("commentId") Long commentId,
            HttpServletRequest request
    ) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        likeService.likeComment(postId,commentId,currentAuthUserId);
    }

    @DeleteMapping("/{id}/comment/{commentId}/like")
    @ResponseStatus(HttpStatus.OK)
    public void unLikeComment(
            @PathVariable("id") Long postId,
            @PathVariable("commentId") Long commentId,
            HttpServletRequest request
    ) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        likeService.unlikeComment(postId,commentId,currentAuthUserId);
    }
}
