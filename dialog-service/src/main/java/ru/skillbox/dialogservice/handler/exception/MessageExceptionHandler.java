package ru.skillbox.dialogservice.handler.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.skillbox.dialogservice.exception.MessageNotFoundException;

@ControllerAdvice
public class MessageExceptionHandler {
    @ExceptionHandler(MessageNotFoundException.class)
    public String handleMessageNotFoundException(MessageNotFoundException e) {
        return e.getMessage();
    }
}
