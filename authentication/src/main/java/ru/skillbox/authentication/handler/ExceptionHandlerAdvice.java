package ru.skillbox.authentication.handler;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.skillbox.authentication.exception.AlreadyExistsException;
import ru.skillbox.authentication.exception.EntityNotFoundException;
import ru.skillbox.authentication.exception.IncorrectRecoveryLinkException;
import ru.skillbox.authentication.exception.RefreshTokenException;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(value = RefreshTokenException.class)
    public ResponseEntity<ErrorResponseBody> refreshTokenExceptionHandler(RefreshTokenException ex) {
        return new ResponseEntity<>(new ErrorResponseBody(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = AlreadyExistsException.class)
    public ResponseEntity<ErrorResponseBody> alreadyExistsExceptionHandler(AlreadyExistsException ex) {
        return new ResponseEntity<>(new ErrorResponseBody(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectRecoveryLinkException.class)
    public ResponseEntity<ErrorResponseBody> incorrectRecoveryLinkExceptionHandler(
            IncorrectRecoveryLinkException ex) {
        return new ResponseEntity<>(new ErrorResponseBody(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseBody> entityNotFoundExceptionHandler(EntityNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponseBody(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

//    private ResponseEntity<ErrorResponseBody> buildResponse(HttpStatus httpStatus,
//                                                            Exception ex,
//                                                            WebRequest webRequest) {
//        return ResponseEntity.status(httpStatus)
//                .body(ErrorResponseBody.builder()
//                        .message(ex.getMessage())
//                        .description(webRequest.getDescription(false))
//                        .build());
//    }
}
