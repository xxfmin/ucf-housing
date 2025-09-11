package com.pm.backend.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ZpidAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleZpidAlreadyExistsException(ZpidAlreadyExistsException ex) {
        log.warn("Zpid already exists: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Zpid already exists");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(AddressAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleAddressAlreadyExistsException(AddressAlreadyExistsException ex) {
        log.warn("Address already exists: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Address already exists");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ListingNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleListingNotFoundException(ListingNotFoundException ex) {
        log.warn("Listing not found: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Listing not found");
        return ResponseEntity.notFound().build();
    }
}
