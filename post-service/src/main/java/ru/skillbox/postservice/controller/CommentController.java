package ru.skillbox.postservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.postservice.dto.CommentDto;
import ru.skillbox.postservice.dto.pages.PageCommentDto;
import ru.skillbox.postservice.dto.pages.Pageable;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.apiPrefix}/post")
public class CommentController {
    @PutMapping("/{id}/comment/{commentId}")
    public ResponseEntity<Object> updateComment(
            @PathVariable("id") Long postId,
            @PathVariable Long commentId,
            @RequestBody CommentDto commentDto
    ) {
        //CommentsService.updateComment(postId,commentId,commentDto)
        return ResponseEntity.status(201).body(null);
    }

    @DeleteMapping("/{id}/comment/{commentId}")
    public ResponseEntity<Object> deleteComment(
            @PathVariable("id") Long postId,
            @PathVariable Long commentId
    ) {
        //CommentsService.delete(postId,commentId)
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{id}/comment")
    public ResponseEntity<PageCommentDto> getCommentsOnPost(
            @PathVariable("id") Long postId,
            @RequestParam Pageable page) {
        //CommentsService.getCommentsOnPost(postId,page)
        return ResponseEntity.ok(new PageCommentDto());
    }
    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> createComment(@PathVariable("id") Long postId) {
        //CommentsService.createComment(postId)
        return ResponseEntity.status(201).body(null);
    }
    @GetMapping("/{id}/comment/{commentId}/subcomment")
    public ResponseEntity<PageCommentDto> getSubComment(
            @PathVariable("id") Long postId,
            @PathVariable("commentId") Long commentId,
            @RequestParam("page") Pageable page
    ) {
        //CommentsService.getSubComment(postId,commentId,page)
        return ResponseEntity.ok(new PageCommentDto());
    }

}
