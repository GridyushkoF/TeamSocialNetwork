package ru.skillbox.postservice.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skillbox.postservice.exception.CommentAccessException;
import ru.skillbox.postservice.model.entity.Comment;
import ru.skillbox.postservice.repository.CommentRepository;

@Component
@RequiredArgsConstructor
public class CommentValidatorUtil {
    private final CommentRepository commentRepository;

    public void throwExceptionIfCommentNotValid(Long commentId) {
        Comment comment = commentRepository.getByIdOrThrowException(commentId);
        if (!isCommentDenied(comment)) {
            throw new CommentAccessException(comment.getId());
        }
    }
    public void throwExceptionIfCommentNotValidWithAuthor(Long commentId, Long userId) {
        Comment comment = commentRepository.getByIdOrThrowException(commentId);
        if (!comment.getAuthorId().equals(userId) || !isCommentDenied(comment)) {
            throw new CommentAccessException(comment.getId());
        }
    }

    private boolean isCommentDenied(Long commentId) {
        return isCommentDenied(commentRepository.getByIdOrThrowException(commentId));
    }

    private boolean isCommentDenied(Comment comment) {
        return !(comment.isBlocked() || comment.isDelete());
    }
}
