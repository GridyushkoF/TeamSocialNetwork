package ru.skillbox.userservice.exceptions;

public class DefaultBadRequestException extends RuntimeException {

    public DefaultBadRequestException(String message) {
        super(message);
    }

}
