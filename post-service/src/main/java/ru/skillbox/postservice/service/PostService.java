package ru.skillbox.postservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.postservice.mapper.PostMapper;
import ru.skillbox.commondto.post.PhotoDto;
import ru.skillbox.commondto.post.PostDto;
import ru.skillbox.commondto.post.PostSearchDto;
import ru.skillbox.commondto.post.pages.PagePostDto;
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
        postToUpdate.setId(postId);
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
    public void createNewPost(PostDto postDto, Long publishDateMillis, Long authUserId) {
        postDto.setAuthorId(authUserId);
        postDto.setId(null);
        Post post = postMapper.postDtoToPost(postDto);
        post.setPublishDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(publishDateMillis), ZoneId.systemDefault()));
        postRepository.save(post);
        log.info("post created by dto " + postDto);
        postMapper.postToPostDto(post);
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