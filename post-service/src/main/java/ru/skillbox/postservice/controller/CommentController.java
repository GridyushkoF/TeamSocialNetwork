package ru.skillbox.postservice.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.commonlib.dto.post.CommentDto;
import ru.skillbox.commonlib.dto.post.pages.PageCommentDto;
import ru.skillbox.postservice.service.CommentService;
import ru.skillbox.commonlib.util.SortCreatorUtil;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.apiPrefix}/post")
@SecurityRequirement(name = "bearerAuth")
public class CommentController {
    private final CommentService commentService;

    @PutMapping("/{id}/comment")
    @ResponseStatus(HttpStatus.OK)
    public void updateComment(
            @PathVariable("id") Long postId,
            @RequestBody CommentDto commentDto,
            @RequestHeader("id") Long currentAuthUserId
    ) {
        commentService.updateComment(postId, commentDto, currentAuthUserId);
    }

    @DeleteMapping("/{id}/comment/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(
            @PathVariable("id") Long postId,
            @PathVariable Long commentId,
            @RequestHeader("id") Long currentAuthUserId
    ) {
        commentService.deleteComment(postId, commentId, currentAuthUserId);
    }

    @GetMapping("/{id}/comment")
    public ResponseEntity<PageCommentDto> getCommentsOnPost(
            @PathVariable("id") Long postId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") List<String> sort,
            @RequestParam(value = "isDeleted", defaultValue = "false") boolean isDeleted,
            @RequestHeader("id") Long currentAuthUserId
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, SortCreatorUtil.createSort(sort));
        return ResponseEntity.ok(commentService.getCommentsOnPost(postId, pageRequest, currentAuthUserId, isDeleted));
    }

    @PostMapping("/{id}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public void createComment(@PathVariable("id") Long postId,
                              @RequestBody CommentDto commentDto,
                              @RequestHeader("id") Long currentAuthUserId
    ) {
        commentService.createNewComment(postId, commentDto, currentAuthUserId);
    }

    @GetMapping("/{id}/comment/{commentId}/subcomment")
    public ResponseEntity<PageCommentDto> getSubComments(
            @PathVariable("id") Long postId,
            @PathVariable("commentId") Long commentId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") List<String> sort,
            @RequestParam(value = "isDeleted", defaultValue = "false") boolean isDeleted,
            @RequestHeader("id") Long currentAuthUserId
    ) {
        Pageable pageable = PageRequest.of(page, size, SortCreatorUtil.createSort(sort));
        return ResponseEntity.ok(commentService.getSubComments(postId, commentId, pageable, currentAuthUserId, isDeleted));
    }

}
