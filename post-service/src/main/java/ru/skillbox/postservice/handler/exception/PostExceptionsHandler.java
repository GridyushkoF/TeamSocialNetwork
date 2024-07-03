package ru.skillbox.postservice.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.skillbox.postservice.exception.PostAccessException;
import ru.skillbox.postservice.exception.PostNotFoundException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class PostExceptionsHandler {

    @ExceptionHandler(value = PostNotFoundException.class)
    public ResponseEntity<Object> handlePostNotFoundException(PostNotFoundException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Post not found!: " + e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = PostAccessException.class)
    public ResponseEntity<Object> handlePostAccessException(PostAccessException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Post denied: " + e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }
}
