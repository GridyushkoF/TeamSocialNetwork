package ru.skillbox.postservice.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.skillbox.commonlib.dto.post.PostDto;
import ru.skillbox.postservice.exception.PostAccessException;
import ru.skillbox.postservice.model.entity.Post;
import ru.skillbox.postservice.repository.PostRepository;

import java.text.MessageFormat;

public class PostValidatorUtilTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostValidatorUtil postValidatorUtil;

    private Post validPost;
    private Post blockedPost;
    private Post deletedPost;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validPost = new Post();
        validPost.setId(1L);
        validPost.setAuthorId(1L);
        validPost.setBlocked(false);
        validPost.setDelete(false);

        blockedPost = new Post();
        blockedPost.setId(2L);
        blockedPost.setAuthorId(1L);
        blockedPost.setBlocked(true);
        blockedPost.setDelete(false);

        deletedPost = new Post();
        deletedPost.setId(3L);
        deletedPost.setAuthorId(1L);
        deletedPost.setBlocked(false);
        deletedPost.setDelete(true);
    }

    @Test
    void isPostDenied_ValidPost_ReturnsFalse() {
        assertFalse(postValidatorUtil.isPostDenied(validPost));
    }

    @Test
    void isPostDenied_BlockedPost_ReturnsTrue() {
        assertTrue(postValidatorUtil.isPostDenied(blockedPost));
    }

    @Test
    void isPostDenied_DeletedPost_ReturnsTrue() {
        assertTrue(postValidatorUtil.isPostDenied(deletedPost));
    }

    @Test
    void isPostDeniedById_ValidPost_ReturnsFalse() {
        when(postRepository.getPostByIdOrThrowException(1L)).thenReturn(validPost);
        assertFalse(postValidatorUtil.isPostDenied(1L));
    }

    @Test
    void isPostDeniedById_BlockedPost_ReturnsTrue() {
        when(postRepository.getPostByIdOrThrowException(2L)).thenReturn(blockedPost);
        assertTrue(postValidatorUtil.isPostDenied(2L));
    }

    @Test
    void isPostDeniedById_DeletedPost_ReturnsTrue() {
        when(postRepository.getPostByIdOrThrowException(3L)).thenReturn(deletedPost);
        assertTrue(postValidatorUtil.isPostDenied(3L));
    }

    @Test
    void throwExceptionIfPostNotValid_ValidPost_NoException() {
        when(postRepository.getPostByIdOrThrowException(1L)).thenReturn(validPost);
        assertDoesNotThrow(() -> postValidatorUtil.throwExceptionIfPostNotValid(1L));
    }

    @Test
    void throwExceptionIfPostNotValid_BlockedPost_ExceptionThrown() {
        when(postRepository.getPostByIdOrThrowException(2L)).thenReturn(blockedPost);
        PostAccessException exception = assertThrows(PostAccessException.class, () -> {
            postValidatorUtil.throwExceptionIfPostNotValid(2L);
        });
        assertEquals(MessageFormat.format(PostAccessException.DEFAULT_MESSAGE, 2L), exception.getMessage());
    }

    @Test
    void throwExceptionIfPostNotValid_DeletedPost_ExceptionThrown() {
        when(postRepository.getPostByIdOrThrowException(3L)).thenReturn(deletedPost);
        PostAccessException exception = assertThrows(PostAccessException.class, () -> {
            postValidatorUtil.throwExceptionIfPostNotValid(3L);
        });
        assertEquals(MessageFormat.format(PostAccessException.DEFAULT_MESSAGE, 3L), exception.getMessage());
    }

    @Test
    void throwAccessExceptionIfUserNotAuthor_ValidAuthor_NoException() {
        PostDto postDto = new PostDto();
        postDto.setId(1L);
        when(postRepository.getPostByIdOrThrowException(1L)).thenReturn(validPost);
        assertDoesNotThrow(() -> postValidatorUtil.throwAccessExceptionIfUserNotAuthor(postDto, 1L));
    }

    @Test
    void throwAccessExceptionIfUserNotAuthor_InvalidAuthor_ExceptionThrown() {
        PostDto postDto = new PostDto();
        postDto.setId(1L);
        when(postRepository.getPostByIdOrThrowException(1L)).thenReturn(validPost);

        PostAccessException exception = assertThrows(PostAccessException.class, () -> {
            postValidatorUtil.throwAccessExceptionIfUserNotAuthor(postDto, 2L);
        });

        assertEquals(MessageFormat.format(PostAccessException.DEFAULT_MESSAGE, 1L), exception.getMessage());
    }
}