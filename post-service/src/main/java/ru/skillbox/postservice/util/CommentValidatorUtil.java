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
        if (!isCommentAllowed(comment)) {
            throw new CommentAccessException(comment.getId());
        }
    }
    public void throwExceptionIfCommentNotValidWithAuthor(Long commentId, Long userId) {
        Comment comment = commentRepository.getByIdOrThrowException(commentId);
        if (!comment.getAuthorId().equals(userId) || !isCommentAllowed(comment)) {
            throw new CommentAccessException(comment.getId());
        }
    }

    public boolean isCommentAllowed(Long commentId) {
        return isCommentAllowed(commentRepository.getByIdOrThrowException(commentId));
    }

    public boolean isCommentAllowed(Comment comment) {
        return !(comment.isBlocked() || comment.isDelete());
    }
}
