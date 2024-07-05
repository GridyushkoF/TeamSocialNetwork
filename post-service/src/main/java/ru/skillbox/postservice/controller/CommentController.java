package ru.skillbox.postservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.commondto.post.CommentDto;
import ru.skillbox.commondto.post.pages.PageCommentDto;
import ru.skillbox.postservice.service.CommentService;
import ru.skillbox.postservice.util.SortCreatorUtil;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.apiPrefix}/post")
public class CommentController {
    private final CommentService commentService;

    @PutMapping("/{id}/comment/{commentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void updateComment(
            @PathVariable("id") Long postId,
            @PathVariable Long commentId,
            @RequestBody CommentDto commentDto,
            HttpServletRequest request
    ) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        commentService.updateComment(postId, commentId, commentDto,currentAuthUserId);
    }

    @DeleteMapping("/{id}/comment/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(
            @PathVariable("id") Long postId,
            @PathVariable Long commentId,
            HttpServletRequest request
    ) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        commentService.deleteComment(postId, commentId,currentAuthUserId);
    }

    @GetMapping("/{id}/comment")
    public ResponseEntity<PageCommentDto> getCommentsOnPost(
            @PathVariable("id") Long postId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") List<String> sort,
            HttpServletRequest request
            )
    {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        PageRequest pageRequest = PageRequest.of(page,size,SortCreatorUtil.createSort(sort));
        return ResponseEntity.ok(commentService.getCommentsOnPost(postId, pageRequest,currentAuthUserId));
    }

    @PostMapping("/{id}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public void createComment(@PathVariable("id") Long postId,
                                                @RequestBody CommentDto commentDto,
                                                HttpServletRequest request) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        commentService.createNewComment(postId, commentDto,currentAuthUserId);
    }

    @GetMapping("/{id}/comment/{commentId}/subcomment")
    public ResponseEntity<PageCommentDto> getSubComments(
            @PathVariable("id") Long postId,
            @PathVariable("commentId") Long commentId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") List<String> sort,
            HttpServletRequest request
    ) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        Pageable pageable = PageRequest.of(page, size, SortCreatorUtil.createSort(sort));
        return ResponseEntity.ok(commentService.getSubComments(postId, commentId, pageable,currentAuthUserId));
    }

}
