package org.example.spring_kr2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler({AnnouncementNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<Map<String, Object>> notFoundExceptionsHandler(Exception ex) {
        Map<String, Object> response = Map.of(
                "status", HttpStatus.NOT_FOUND,
                "message", ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler({IllegalCategoryException.class, CancelledAnnouncementSetEmployee.class})
    public ResponseEntity<Map<String, Object>> badRequestExceptionHandler(Exception ex) {
        Map<String, Object> response = Map.of(
                "status", HttpStatus.BAD_REQUEST,
                "message", ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
