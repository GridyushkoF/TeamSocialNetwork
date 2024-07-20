package ru.skillbox.userservice.exception;

public class NoFriendshipFoundException extends RuntimeException {
    public NoFriendshipFoundException(String message) {
        super(message);
    }
}
