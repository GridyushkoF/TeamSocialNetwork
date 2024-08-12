package ru.skillbox.postservice.exception;

import java.text.MessageFormat;

public class PostAccessException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "Post with id {0} is not allowed!";
    public PostAccessException(String message) {
        super(message);
    }
    public PostAccessException(Long postId) {
        super(MessageFormat.format(DEFAULT_MESSAGE,postId));
    }
}
