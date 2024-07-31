package ru.skillbox.postservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.apiPrefix}/post")
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
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sort") List<String> sort,
            HttpServletRequest request) {
        if (page == -1) page = 0;
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        return ResponseEntity.ok(
                postService.searchPosts(searchDto, PageRequest.of(page, size, SortCreatorUtil.createSort(sort)), currentAuthUserId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createPost(
            @RequestBody PostDto postDto, HttpServletRequest request) {
        postService.createNewPost(postDto, request);
    }

    @PostMapping("/storagePostPhoto")
    public ResponseEntity<PhotoDto> uploadPhotoToStorage(
            @RequestParam(value = "file") MultipartFile file
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.uploadImage(file));
    }
}

