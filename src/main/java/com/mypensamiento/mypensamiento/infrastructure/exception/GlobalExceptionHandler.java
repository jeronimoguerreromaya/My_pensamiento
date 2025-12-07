package com.mypensamiento.mypensamiento.infrastructure.exception;

import com.mypensamiento.mypensamiento.application.exception.EmailAlreadyExistsException;
import com.mypensamiento.mypensamiento.application.exception.FieldValidationException;
import com.mypensamiento.mypensamiento.application.exception.NickNameAlreadyExistsException;
import com.mypensamiento.mypensamiento.application.exception.UserNotFoundException;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.swing.text.html.parser.Entity;
import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(FieldValidationException.class)
    public ResponseEntity<Map<String, Object>> handleFieldValidationException(FieldValidationException e) {
        log.warn("Validation error: {}", e.getMessage());
        Map<String, Object> response = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Bad Request",
                "message", e.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException e) {
        log.warn("User not found: {}", e.getMessage());
        Map<String, Object> response = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.NOT_FOUND.value(),
                "error", "Not Found",
                "message", e.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(NickNameAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleNickNameAlreadyExistsException(NickNameAlreadyExistsException e) {

        log.warn("Attempted duplicate registration: {}", e.getMessage());

        Map<String, Object> response = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.CONFLICT.value(),
                "error", "Conflict",
                "message", e.getMessage()
        );

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);

    }


    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {

        log.warn("Attempted duplicate registration: {}", e.getMessage());

        Map<String, Object> response = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.CONFLICT.value(),
                "error", "Conflict",
                "message", e.getMessage()
        );

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);

    }

}
