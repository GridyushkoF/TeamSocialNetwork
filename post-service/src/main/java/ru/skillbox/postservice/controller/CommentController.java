package ru.skillbox.postservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
@Tag(name = "Comment Controller", description = "Comment API")
public class CommentController {
    private final CommentService commentService;

    @PutMapping("/{id}/comment")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update comment")
    public void updateComment(
            @PathVariable("id") Long postId,
            @RequestBody CommentDto commentDto,
            HttpServletRequest request
    ) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        commentService.updateComment(postId, commentDto, currentAuthUserId);
    }

    @DeleteMapping("/{id}/comment/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete comment")
    public void deleteComment(
            @PathVariable("id") Long postId,
            @PathVariable Long commentId,
            HttpServletRequest request
    ) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        commentService.deleteComment(postId, commentId, currentAuthUserId);
    }

    @GetMapping("/{id}/comment")
    @Operation(summary = "Get comment on post")
    public ResponseEntity<PageCommentDto> getCommentsOnPost(
            @PathVariable("id") Long postId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") List<String> sort,
            @RequestParam(value = "isDeleted", defaultValue = "false") boolean isDeleted,
            HttpServletRequest request
    ) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        PageRequest pageRequest = PageRequest.of(page, size, SortCreatorUtil.createSort(sort));
        return ResponseEntity.ok(commentService.getCommentsOnPost(postId, pageRequest, currentAuthUserId, isDeleted));
    }

    @PostMapping("/{id}/comment")
    @Operation(summary = "Create comment")
    @ResponseStatus(HttpStatus.CREATED)
    public void createComment(@PathVariable("id") Long postId,
                              @RequestBody CommentDto commentDto,
                              HttpServletRequest request) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        commentService.createNewComment(postId, commentDto, currentAuthUserId);
    }

    @GetMapping("/{id}/comment/{commentId}/subcomment")
    @Operation(summary = "Get subcomments")
    public ResponseEntity<PageCommentDto> getSubComments(
            @PathVariable("id") Long postId,
            @PathVariable("commentId") Long commentId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") List<String> sort,
            @RequestParam(value = "isDeleted", defaultValue = "false") boolean isDeleted,
            HttpServletRequest request
    ) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        Pageable pageable = PageRequest.of(page, size, SortCreatorUtil.createSort(sort));
        return ResponseEntity.ok(commentService.getSubComments(postId, commentId, pageable, currentAuthUserId, isDeleted));
    }

}
