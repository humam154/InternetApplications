package com.humam.security.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponse<String>> handleValidationException(MethodArgumentNotValidException ex) {
        
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("Validation failed");

        return ResponseEntity.badRequest().body(GenericResponse.error(errorMessage));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<GenericResponse<String>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(GenericResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<String>> handleGenericException(Exception ex) {
        return ResponseEntity.internalServerError()
                .body(GenericResponse.error("An unexpected error occurred: " + ex.getMessage()));
    }
}
