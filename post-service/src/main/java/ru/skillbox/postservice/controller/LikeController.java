package ru.skillbox.postservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.commonlib.dto.post.LikeDto;
import ru.skillbox.postservice.service.LikeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.apiPrefix}/post")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Storage Controller", description = "Storage API")
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/{id}/like")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Like post")
    public void likePost(
            @PathVariable("id") Long postId,
            @RequestBody LikeDto likeDto,
            @RequestHeader("id") Long currentAuthUserId) {
        likeService.likePost(postId, likeDto, currentAuthUserId);
    }

    @DeleteMapping("/{id}/like")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Unlike post")
    public void unLikePost(
            @PathVariable("id") Long postId,
            @RequestHeader("id") Long currentAuthUserId) {
        likeService.unlikePost(postId, currentAuthUserId);
    }

    @PostMapping("/{id}/comment/{commentId}/like")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Like comment")
    public void likeComment(
            @PathVariable("id") Long postId,
            @PathVariable("commentId") Long commentId,
            @RequestHeader("id") Long currentAuthUserId) {
        likeService.likeComment(postId, commentId, currentAuthUserId);
    }

    @DeleteMapping("/{id}/comment/{commentId}/like")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Unlike post")
    public void unLikeComment(
            @PathVariable("id") Long postId,
            @PathVariable("commentId") Long commentId,
            @RequestHeader("id") Long currentAuthUserId) {
        likeService.unlikeComment(postId, commentId, currentAuthUserId);
    }
}
