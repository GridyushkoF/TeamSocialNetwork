package ru.skillbox.postservice.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.skillbox.postservice.exception.LikeException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class LikeExceptionHandler {
    @ExceptionHandler(value = LikeException.class)
    public ResponseEntity<Object> handleCommentNotFoundException(LikeException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Like error: " + e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
