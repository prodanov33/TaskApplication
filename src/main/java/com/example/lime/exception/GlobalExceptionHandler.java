package com.example.lime.exception;

import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> "Field '" + error.getField() + "': " + error.getDefaultMessage())
                .collect(Collectors.toList());

        logger.error("Validation failed: {}", errors);

        ErrorResponse errorResponse = new ErrorResponse(
                "Validation failed",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                errors
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        logger.error("Invalid request body: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                "Invalid request body",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                List.of(ex.getMessage())
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {

        logger.error("Resource Not Found: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                "Resource Not Found",
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                List.of(ex.getMessage())
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {

        logger.error("Invalid Argument Type: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                "Invalid Argument Type",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                List.of(ex.getMessage())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
