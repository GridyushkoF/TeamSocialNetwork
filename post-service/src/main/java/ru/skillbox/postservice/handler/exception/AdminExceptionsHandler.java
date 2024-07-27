package ru.skillbox.postservice.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.skillbox.postservice.exception.AdminAccessException;

import java.util.Map;

@ControllerAdvice
public class AdminExceptionsHandler {
    @ExceptionHandler(value = AdminAccessException.class)
    public ResponseEntity<Object> handleCommentNotFoundException(AdminAccessException e) {
        return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
