package ru.skillbox.postservice.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(String message) {
        super(message);
    }
    public CommentNotFoundException(Long commentId) {
        super("Can`t find comment with id " + commentId);
    }
}
