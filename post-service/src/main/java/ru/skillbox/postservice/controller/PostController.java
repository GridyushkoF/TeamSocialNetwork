package ru.skillbox.postservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.postservice.dto.PhotoDto;
import ru.skillbox.postservice.dto.PostDto;
import ru.skillbox.postservice.dto.PostSearchDto;
import ru.skillbox.postservice.dto.pages.PagePostDto;
import ru.skillbox.postservice.dto.pages.Pageable;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.apiPrefix}/post")
public class PostController {

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") Long postId) {
        //PostService.getPostDtoById(postId)
        return ResponseEntity.ok(new PostDto());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePostById(
            @PathVariable("id") Long postId,
            @RequestBody PostDto postDto) {
        //PostService.updatePostById(postId)
        return ResponseEntity.status(201).body(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePostById(@PathVariable("id") Long postId) {
        //PostService.deletePostById(postId)
        return ResponseEntity.ok(null);
    }

    @GetMapping("/")
    public ResponseEntity<PagePostDto> searchPosts(
            @RequestParam PostSearchDto searchDto,
            @RequestParam Pageable page
    ) {
        //PostService.searchPosts(searchDto,page)
        return ResponseEntity.ok(new PagePostDto());
    }

    @PostMapping("/")
    public ResponseEntity<PostDto> createPost(
            @RequestParam(value = "publishDate", required = false) Long publishDateEpochMillis,
            @RequestBody PostDto postDto
    ) {
        //PostService.createNewPost(publishDateEpochMillis,postDto)
        return ResponseEntity.status(201).body(new PostDto());
    }

    @PostMapping("/storagePostPhoto")
    public ResponseEntity<PhotoDto> uploadPhotoToStorage(
            @RequestParam(value = "file") String binaryFile
    ) {
        //PostService.uploadPhoto(fileBinary)
        return ResponseEntity.status(201).body(new PhotoDto());
    }
}

