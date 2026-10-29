package org.example.spring_kr2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    // 404 — сущность не найдена
    @ExceptionHandler({AnnouncementNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<Map<String, Object>> notFoundExceptionsHandler(Exception ex) {
        Map<String, Object> response = Map.of(
                "status", HttpStatus.NOT_FOUND,
                "message", ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler({
            IllegalCategoryException.class,
            CancelledAnnouncementSetEmployee.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<Map<String, Object>> badRequestExceptionHandler(Exception ex) {
        Map<String, Object> response = Map.of(
                "status", HttpStatus.BAD_REQUEST,
                "message", ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> alreadyExistsHandler(Exception ex) {
        Map<String, Object> response = Map.of(
                "status", HttpStatus.CONFLICT,
                "message", ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST);

        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Ошибка валидации")
                .findFirst()
                .orElse("Ошибка валидации");

        response.put("message", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}