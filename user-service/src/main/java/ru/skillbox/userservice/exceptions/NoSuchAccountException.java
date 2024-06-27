package ru.skillbox.userservice.exceptions;

public class NoSuchAccountException extends RuntimeException {

    public NoSuchAccountException(String message) {
        super(message);
    }
}
