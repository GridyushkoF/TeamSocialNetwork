package ru.skillbox.postservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.commonlib.dto.post.PostDto;
import ru.skillbox.commonlib.dto.post.PostSearchDto;
import ru.skillbox.commonlib.dto.post.PostType;
import ru.skillbox.commonlib.dto.post.pages.PagePostDto;
import ru.skillbox.postservice.service.PostService;
import ru.skillbox.commonlib.util.SortCreatorUtil;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.apiPrefix}/post")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Post Controller", description = "Post API")
public class PostController {
    private final PostService postService;

    @GetMapping("/{id}")
    @Operation(summary = "Get post by ID")
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") Long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Update post by ID")
    public void updatePostById(
            @RequestBody PostDto postDto,
            @RequestHeader("id") Long currentAuthUserId) {
        postService.updatePost(postDto, currentAuthUserId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete post by ID")
    public void deletePostById(@PathVariable("id") Long postId,
                               @RequestHeader("id") Long currentAuthUserId) {
        postService.deletePostById(postId, currentAuthUserId);
    }

    @GetMapping
    @Operation(summary = "Search post")
    public ResponseEntity<PagePostDto> searchPosts(
            @RequestParam(value = "ids", required = false) List<Integer> ids,
            @RequestParam(value = "accountIds", required = false) List<Integer> accountIds,
            @RequestParam(value = "blockedIds", required = false) List<Integer> blockedIds,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "postText", required = false) String postText,
            @RequestParam(value = "withFriends", required = false) Boolean withFriends,
            @RequestParam(value = "isDeleted", required = false) Boolean isDelete,
            @RequestParam(value = "tags", required = false) List<String> tags,
            @RequestParam(value = "dateFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateFrom,
            @RequestParam(value = "dateTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateTo,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sort") List<String> sort,
            @RequestHeader("id") Long currentAuthUserId) {
        if (page == -1) {
            page = 0;
        }

        PostSearchDto searchDto = PostSearchDto.builder()
                .ids(ids)
                .accountIds(accountIds)
                .blockedIds(blockedIds)
                .author(author)
                .title(title)
                .postText(postText)
                .withFriends(withFriends)
                .isDeleted(isDelete)
                .tags(tags)
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .build();

        return ResponseEntity.ok(
                postService.searchPosts(searchDto, PageRequest.of(page, size, SortCreatorUtil.createSort(sort)), currentAuthUserId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create post")
    public void createPost(
            @RequestParam(value = "publishDate", required = false) Long publishDateEpochMillis,
            @RequestBody PostDto postDto,
            @RequestHeader("id") Long currentAuthUserId) {
        if (Objects.isNull(publishDateEpochMillis)) {
            postDto.setType(PostType.POSTED);
        } else {
            postDto.setType(PostType.QUEUED);
        }
        postService.createNewPost(postDto, currentAuthUserId);
    }
}

