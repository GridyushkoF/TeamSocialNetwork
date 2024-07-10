package ru.skillbox.userservice.exception;

public class NoSuchAccountException extends RuntimeException {

    public NoSuchAccountException(String message) {
        super(message);
    }
}
