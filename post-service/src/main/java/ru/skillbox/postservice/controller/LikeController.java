package ru.skillbox.postservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.apiPrefix}/post")
public class LikeController {
    @PostMapping("/{id}/like")
    public ResponseEntity<Object> likePost(
            @PathVariable("id") Long postId
    ) {
        //LikeService.likePost(postId)
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    @DeleteMapping("/{id}/like")
    public ResponseEntity<Object> unLikePost(
            @PathVariable("id") Long postId
    ) {
        //LikeService.unLikePost(postId)
        return ResponseEntity.ok(null);
    }
    @PostMapping("/{id}/comment/{commentId}/like")
    public ResponseEntity<Object> likeComment(
            @PathVariable("id") Long postId,
            @PathVariable("commentId") Long commentId
    ) {
        //LikeService.likeComment(postId,commentId)
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    @DeleteMapping("/{id}/comment/{commentId}/like")
    public ResponseEntity<Object> unLikeComment(
            @PathVariable("id") Long postId,
            @PathVariable("commentId") Long commentId
    ) {
        //LikeService.unlikeComment(postId,commentId)
        return ResponseEntity.ok(null);
    }
}
