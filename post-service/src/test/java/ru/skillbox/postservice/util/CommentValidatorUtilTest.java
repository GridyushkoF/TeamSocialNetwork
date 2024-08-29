package ru.skillbox.postservice.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.skillbox.commonlib.dto.post.CommentType;
import ru.skillbox.postservice.exception.CommentAccessException;
import ru.skillbox.postservice.model.entity.Comment;
import ru.skillbox.postservice.repository.CommentRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CommentValidatorUtilTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentValidatorUtil commentValidatorUtil;

    private Comment validComment;
    private Comment blockedComment;
    private Comment deletedComment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        validComment = new Comment();
        validComment.setId(1L);
        validComment.setAuthorId(1L);
        validComment.setBlocked(false);
        validComment.setDelete(false);
        validComment.setCommentType(CommentType.POST);

        blockedComment = new Comment();
        blockedComment.setId(2L);
        blockedComment.setAuthorId(1L);
        blockedComment.setBlocked(true);
        blockedComment.setDelete(false);
        blockedComment.setCommentType(CommentType.POST);

        deletedComment = new Comment();
        deletedComment.setId(3L);
        deletedComment.setAuthorId(1L);
        deletedComment.setBlocked(false);
        deletedComment.setDelete(true);
        deletedComment.setCommentType(CommentType.POST);
    }

    @Test
    void throwExceptionIfCommentNotValid_ValidComment_NoException() {
        when(commentRepository.getByIdOrThrowException(1L)).thenReturn(validComment);

        assertDoesNotThrow(() -> commentValidatorUtil.throwExceptionIfCommentNotValid(1L));
    }

    @Test
    void throwExceptionIfCommentNotValid_BlockedComment_ExceptionThrown() {
        when(commentRepository.getByIdOrThrowException(2L)).thenReturn(blockedComment);

        CommentAccessException exception = assertThrows(CommentAccessException.class, () -> {
            commentValidatorUtil.throwExceptionIfCommentNotValid(2L);
        });

        assertEquals("Can`t find comment with id 2", exception.getMessage());
    }

    @Test
    void throwExceptionIfCommentNotValid_DeletedComment_ExceptionThrown() {
        when(commentRepository.getByIdOrThrowException(3L)).thenReturn(deletedComment);

        CommentAccessException exception = assertThrows(CommentAccessException.class, () -> {
            commentValidatorUtil.throwExceptionIfCommentNotValid(3L);
        });

        assertEquals("Can`t find comment with id 3", exception.getMessage());
    }

    @Test
    void throwExceptionIfCommentNotValidWithAuthor_ValidComment_NoException() {
        when(commentRepository.getByIdOrThrowException(1L)).thenReturn(validComment);

        assertDoesNotThrow(() -> commentValidatorUtil.throwExceptionIfCommentNotValidWithAuthor(1L, 1L));
    }

    @Test
    void throwExceptionIfCommentNotValidWithAuthor_InvalidAuthor_ExceptionThrown() {
        when(commentRepository.getByIdOrThrowException(1L)).thenReturn(validComment);

        CommentAccessException exception = assertThrows(CommentAccessException.class, () -> {
            commentValidatorUtil.throwExceptionIfCommentNotValidWithAuthor(1L, 2L);
        });

        assertEquals("Can`t find comment with id 1", exception.getMessage());
    }

    @Test
    void throwExceptionIfCommentNotValidWithAuthor_BlockedComment_ExceptionThrown() {
        when(commentRepository.getByIdOrThrowException(2L)).thenReturn(blockedComment);

        CommentAccessException exception = assertThrows(CommentAccessException.class, () -> {
            commentValidatorUtil.throwExceptionIfCommentNotValidWithAuthor(2L, 1L);
        });

        assertEquals("Can`t find comment with id 2", exception.getMessage());
    }

    @Test
    void isCommentAllowed_ValidComment_ReturnsTrue() {
        when(commentRepository.getByIdOrThrowException(1L)).thenReturn(validComment);

        assertTrue(commentValidatorUtil.isCommentAllowed(1L));
    }

    @Test
    void isCommentAllowed_BlockedComment_ReturnsFalse() {
        when(commentRepository.getByIdOrThrowException(2L)).thenReturn(blockedComment);

        assertFalse(commentValidatorUtil.isCommentAllowed(2L));
    }

    @Test
    void isCommentAllowed_DeletedComment_ReturnsFalse() {
        when(commentRepository.getByIdOrThrowException(3L)).thenReturn(deletedComment);

        assertFalse(commentValidatorUtil.isCommentAllowed(3L));
    }
}