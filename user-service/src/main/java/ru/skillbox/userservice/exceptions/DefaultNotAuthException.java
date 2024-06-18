package ru.skillbox.userservice.exceptions;

public class DefaultNotAuthException extends RuntimeException {

    public DefaultNotAuthException(String message) {
        super(message);
    }
}
