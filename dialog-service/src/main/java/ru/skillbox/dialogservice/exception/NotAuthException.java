package ru.skillbox.dialogservice.exception;

public class NotAuthException extends RuntimeException {

    public NotAuthException(String message) {
        super(message);
    }
}
