package rw.ac.auca.pharmacy.pharmacy_inventory.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DuplicateHandler {

    private static final String DUPLICATE_MESSAGE = "A record with this unique value already exists.";

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleDuplicate(IllegalArgumentException exception) {
        String message = exception.getMessage();
        if (message != null && message.contains("already exists")) {
            message = DUPLICATE_MESSAGE;
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleConstraintViolation(DataIntegrityViolationException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(DUPLICATE_MESSAGE);
    }
}
