package ru.skillbox.postservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.commondto.post.PhotoDto;
import ru.skillbox.commondto.post.PostDto;
import ru.skillbox.commondto.post.PostSearchDto;
import ru.skillbox.commondto.post.pages.PagePostDto;
import ru.skillbox.postservice.service.PostService;
import ru.skillbox.postservice.util.SortCreatorUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.apiPrefix}/post")
public class PostController {
    private final PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") Long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void updatePostById(
            @PathVariable("id") Long postId,
            @RequestBody PostDto postDto,
            HttpServletRequest request) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        postService.updatePost(postDto,postId,currentAuthUserId);
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
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sorted") boolean sorted,
            @RequestParam("unsorted") boolean unsorted,
            @RequestParam("empty") boolean isEmpty
    ) {
        Sort sort = SortCreatorUtil.createSort(isEmpty,sorted,unsorted);
        return ResponseEntity.ok(
                postService.searchPosts(searchDto, PageRequest.of(page, size, sort)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createPost(
            @RequestParam(value = "publishDate", required = false) Long publishDateEpochMillis,
            @RequestBody PostDto postDto,
            HttpServletRequest request

    ) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        postService.createNewPost(postDto, publishDateEpochMillis,currentAuthUserId);
    }

    @PostMapping("/storagePostPhoto")
    public ResponseEntity<PhotoDto> uploadPhotoToStorage(
            @RequestParam(value = "file") MultipartFile file
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.uploadImage(file));
    }
}

