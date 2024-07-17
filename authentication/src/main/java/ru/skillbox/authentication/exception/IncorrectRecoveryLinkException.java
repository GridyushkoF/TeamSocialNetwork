package ru.skillbox.authentication.exception;

public class IncorrectRecoveryLinkException extends RuntimeException {
    public IncorrectRecoveryLinkException(String message) {
        super(message);
    }
}
