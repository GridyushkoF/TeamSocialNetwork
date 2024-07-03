package ru.skillbox.postservice.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.skillbox.postservice.exception.CommentAccessException;
import ru.skillbox.postservice.exception.CommentNotFoundException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CommentExceptionsHandler {
    @ExceptionHandler(value = CommentNotFoundException.class)
    public ResponseEntity<Object> handleCommentNotFoundException(CommentNotFoundException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Comment not found!: " + e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = CommentAccessException.class)
    public ResponseEntity<Object> handleCommentNotFoundException(CommentAccessException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Comment access denied!: " + e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }
}