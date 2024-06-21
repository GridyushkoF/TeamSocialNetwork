package ru.skillbox.postservice.exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException (String errorMessage) {
        super(errorMessage);
    }
}
