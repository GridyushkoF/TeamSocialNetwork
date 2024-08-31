package ru.skillbox.postservice.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.commonlib.dto.post.PostDto;
import ru.skillbox.commonlib.dto.post.PostSearchDto;
import ru.skillbox.commonlib.dto.post.PostType;
import ru.skillbox.commonlib.dto.post.pages.PagePostDto;
import ru.skillbox.postservice.service.PostService;
import ru.skillbox.commonlib.util.SortCreatorUtil;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.apiPrefix}/post")
@SecurityRequirement(name = "bearerAuth")
public class PostController {
    private final PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") Long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void updatePostById(
            @RequestBody PostDto postDto,
            HttpServletRequest request) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        postService.updatePost(postDto,currentAuthUserId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePostById(@PathVariable("id") Long postId,
                                                 HttpServletRequest request) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        postService.deletePostById(postId,currentAuthUserId);
    }

    @GetMapping
    public ResponseEntity<PagePostDto> searchPosts(
            @ModelAttribute PostSearchDto searchDto,
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "5") int size,
            @RequestParam(value = "sort") List<String> sort,
            HttpServletRequest request
    ) {
        if(page == -1) {
            page = 0;
        }
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        return ResponseEntity.ok(
                postService.searchPosts(searchDto, PageRequest.of(page,size,SortCreatorUtil.createSort(sort)),currentAuthUserId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createPost(
            @RequestParam(value = "publishDate", required = false) Long publishDateEpochMillis,
            @RequestBody PostDto postDto,
            HttpServletRequest request

    ) {
        if(Objects.isNull(publishDateEpochMillis)) {
            postDto.setType(PostType.POSTED);
        } else {
            postDto.setType(PostType.QUEUED);
        }
        postService.createNewPost(postDto, request);
    }

}

