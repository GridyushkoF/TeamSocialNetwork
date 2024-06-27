package ru.skillbox.userservice.exceptions;

public class NotAuthException extends RuntimeException {

    public NotAuthException(String message) {
        super(message);
    }
}
