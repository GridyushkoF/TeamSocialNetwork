package ru.skillbox.postservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.postservice.exception.PostAccessException;
import ru.skillbox.postservice.mapper.PostMapper;
import ru.skillbox.postservice.model.dto.PhotoDto;
import ru.skillbox.postservice.model.dto.PostDto;
import ru.skillbox.postservice.model.dto.PostSearchDto;
import ru.skillbox.postservice.model.dto.pages.PagePostDto;
import ru.skillbox.postservice.model.entity.LikeEntityType;
import ru.skillbox.postservice.model.entity.Post;
import ru.skillbox.postservice.repository.CommentRepository;
import ru.skillbox.postservice.repository.LikeRepository;
import ru.skillbox.postservice.repository.PostRepository;
import ru.skillbox.postservice.util.PostValidatorUtil;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class PostService {
    private final PostRepository postRepository;
    private final PostValidatorUtil postValidator;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final PostMapper postMapper;

    @Transactional
    public PostDto getPostById(Long postId) {
        postValidator.throwExceptionIfPostNotValid(postId);
        Post post = postRepository.getPostByIdOrThrowException(postId);
        return postMapper.postToPostDto(post);
    }

    @Transactional
    public void updatePost(PostDto postToUpdate, Long postId, Long authUserId) {
        postValidator.throwAccessExceptionIfUserNotAuthor(postToUpdate, authUserId);
        postValidator.throwExceptionIfPostNotValid(postId);
        postRepository.save(postMapper.postDtoToPost(postToUpdate));
        log.info("post with id " + postId + " was updated by postDto: " + postToUpdate);
    }


    @Transactional
    public void deletePostById(Long postId, Long authUserId) {
        Post post = postRepository.getPostByIdOrThrowException(postId);
        postValidator.throwAccessExceptionIfUserNotAuthor(postMapper.postToPostDto(post), authUserId);
        post.setDelete(true);
        likeRepository.deleteAll(likeRepository.findAllByEntityTypeAndEntityId(LikeEntityType.POST,postId));
        commentRepository.findAllByPostId(postId).forEach(comment -> {
            comment.setDelete(true);
            likeRepository.findAllByEntityTypeAndEntityId(LikeEntityType.COMMENT,comment.getId());
        });
        postRepository.save(post);
        log.info("post marked as deleted  " + postId + " attached comments marked as deleted and all likes deleted ");
    }

    @Transactional
    public PagePostDto searchPosts(PostSearchDto postSearchDto, Pageable pageable) {
        //Without specification api, pageable only
        Page<Post> postsPage = postRepository.findAll(pageable);
        List<PostDto> content = postsPage.get().map(postMapper::postToPostDto).toList();
        return PagePostDto.builder()
                .totalElements(postsPage.getTotalElements())
                .totalPages(postsPage.getTotalPages())
                .number(postsPage.getNumber())
                .size(postsPage.getSize())
                .content(content)
                .sort(postsPage.getSort())
                .first(postsPage.isFirst())
                .last(postsPage.isLast())
                .numberOfElements(postsPage.getNumberOfElements())
                .pageable(pageable)
                .empty(postsPage.isEmpty())
                .build();
    }

    @Transactional
    public PostDto createNewPost(PostDto postDto, Long publishDateMillis, Long authUserId) {
        Post post = postMapper.postDtoToPost(postDto);
        if(!authUserId.equals(post.getAuthorId())) {
            throw new PostAccessException(post.getId());
        }
        post.setPublishDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(publishDateMillis), ZoneId.systemDefault()));
        postRepository.save(post);
        log.info("post created by dto " + postDto);
        return postMapper.postToPostDto(post);
    }

    public PhotoDto uploadImage(MultipartFile multipartFile) {
        //demo
        try {
            String originalFileName = multipartFile.getOriginalFilename();
            assert originalFileName != null;
            String newFileName = UUID.randomUUID() + originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
            multipartFile.transferTo(new File("/images/" + newFileName));
            return new PhotoDto(newFileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("error when saving file");
        }
    }
}