package ru.skillbox.postservice.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.commonlib.dto.post.PostType;
import ru.skillbox.postservice.mapper.PostMapper;
import ru.skillbox.commonlib.dto.post.PhotoDto;
import ru.skillbox.commonlib.dto.post.PostDto;
import ru.skillbox.commonlib.dto.post.PostSearchDto;
import ru.skillbox.commonlib.dto.post.pages.PagePostDto;
import ru.skillbox.postservice.mapper.PostMapperDecorator;
import ru.skillbox.postservice.model.entity.LikeEntityType;
import ru.skillbox.postservice.model.entity.Post;
import ru.skillbox.postservice.processor.PostProcessor;
import ru.skillbox.postservice.repository.CommentRepository;
import ru.skillbox.postservice.repository.LikeRepository;
import ru.skillbox.postservice.repository.PostRepository;
import ru.skillbox.postservice.service.specification_api.PostSpecificationService;
import ru.skillbox.postservice.util.ColumnsUtil;
import ru.skillbox.postservice.util.PostValidatorUtil;

import java.io.File;
import java.time.*;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Log4j2
public class PostService {
    private final PostRepository postRepository;
    private final PostValidatorUtil postValidator;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final PostMapper postMapper;
    private final PostSpecificationService postSpecificationService;
    private final PostMapperDecorator postMapperDecorator;
    private final PostProcessor processor;

    @Transactional
    public PostDto getPostById(Long postId) {
        postValidator.throwExceptionIfPostNotValid(postId);
        Post post = postRepository.getPostByIdOrThrowException(postId);
        return postMapper.postToPostDto(post);
    }

    @Transactional
    public void updatePost(PostDto postToUpdate, Long authUserId) {
        postValidator.throwAccessExceptionIfUserNotAuthor(postToUpdate, authUserId);
        postValidator.throwExceptionIfPostNotValid(postToUpdate.getId());
        Post existingPost = postRepository.getPostByIdOrThrowException(postToUpdate.getId());
        BeanUtils.copyProperties(postToUpdate, existingPost, ColumnsUtil.getNullPropertyNames(postToUpdate));
        existingPost.setBlocked(postToUpdate.isBlocked());
        existingPost.setDelete(postToUpdate.isDelete());
        existingPost.setTags(postToUpdate.getTags() != null ? postMapperDecorator.convertTagsAndGet(postToUpdate, existingPost).getTags() : existingPost.getTags());
        postRepository.save(existingPost);
        log.info("post with id " + postToUpdate.getId() + " was updated by postDto: " + postToUpdate);
    }


    @Transactional
    public void deletePostById(Long postId, Long authUserId) {
        Post post = postRepository.getPostByIdOrThrowException(postId);
        postValidator.throwAccessExceptionIfUserNotAuthor(postMapper.postToPostDto(post), authUserId);
        post.setDelete(true);
        likeRepository.deleteAll(likeRepository.findAllByEntityTypeAndEntityId(LikeEntityType.POST, postId));
        commentRepository.findAllByPostId(postId).forEach(comment -> {
            comment.setDelete(true);
            likeRepository.findAllByEntityTypeAndEntityId(LikeEntityType.COMMENT, comment.getId());
        });
        postRepository.save(post);
        log.info("post marked as deleted  " + postId + " attached comments marked as deleted and all likes deleted ");
    }

    @Transactional
    public PagePostDto searchPosts(PostSearchDto postSearchDto, Pageable pageable, Long userId) {
        Specification<Post> postSpecification =
                postSpecificationService.getSpecificationByDto(postSearchDto, userId);
        Page<Post> postsPage = postRepository.findAll(postSpecification, pageable);
        List<PostDto> content = postsPage.get().map(postMapper::postToPostDto).toList();
        content = content.stream()
                .peek(postDto -> {
                    boolean myLike = likeRepository.existsByEntityTypeAndEntityIdAndUserId(LikeEntityType.POST, postDto.getId(), userId);
                    postDto.setMyLike(myLike);
                    Long likesAmount = likeRepository.countAllByEntityTypeAndEntityId(LikeEntityType.POST, postDto.getId());
                    postDto.setLikeAmount(likesAmount);
                    Long commentsCount = commentRepository.countByPostId(postDto.getId());
                    postDto.setCommentsCount(commentsCount);
                }).toList();
        return buildPagePostDto(pageable, postsPage, content);
    }

    private static PagePostDto buildPagePostDto(Pageable pageable, Page<Post> postsPage, List<PostDto> content) {
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
    public void createNewPost(PostDto postDto, HttpServletRequest request) {
        Long currentAuthUserId = Long.parseLong(request.getHeader("id"));
        if (postDto.getPublishDate() != null) {
            postDto.setType(PostType.QUEUED);
        } else {
            postDto.setType(PostType.POSTED);
        }
        postDto.setAuthorId(currentAuthUserId);
        postDto.setId(null);
        Post post = postMapper.postDtoToPost(postDto);
        Post newPost = postRepository.save(post);
        log.info("post created by dto " + postDto);
        if (newPost.getType().equals(PostType.POSTED)) {
            processor.process(newPost);
        }
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    @Transactional
    public void postQueuedPosts() {
        postRepository.findAllByTypeAndPublishDateLessThan(PostType.QUEUED, ZonedDateTime.now())
                .forEach(post -> {
                    post.setType(PostType.POSTED);
                    Post newPost = postRepository.save(post);
                    log.info("post updated by scheduler " + newPost);
                    processor.process(newPost);
                });
    }

    public PhotoDto uploadImage(MultipartFile multipartFile) {
        //demo
        try {
            File imagesDir = new File("/images/");
            if (!imagesDir.exists()) {
                imagesDir.mkdir();
            }
            String originalFileName = multipartFile.getOriginalFilename();
            assert originalFileName != null;
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String newFileName = UUID.randomUUID() + fileExtension;
            String filePath = imagesDir.getPath() + File.separator + newFileName;

            multipartFile.transferTo(new File(filePath));
            return new PhotoDto(newFileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("error when saving file");
        }
    }

}