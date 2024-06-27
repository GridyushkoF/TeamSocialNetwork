package ru.skillbox.postservice.exception;

public class CommentAccessException extends RuntimeException{
    public CommentAccessException(String message) {
        super(message);
    }
    public CommentAccessException(Long commentId) {
        super("Can`t find comment with id " + commentId);
    }
}
