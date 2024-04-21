package ru.practicum.shareit.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.common.exception.EmailValidationExcepotion;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.exception.ValidationException;

import java.util.Map;

@ControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(final ValidationException e) {
        log.error("400 Ошибка валидации: {}", e.getMessage());
        return new ResponseEntity<>(
                Map.of("error", e.getMessage(),
                        "errorMessage", e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({RuntimeException.class, EmailValidationExcepotion.class})
    public ResponseEntity<Map<String, String>> handleException(final RuntimeException e) {
        log.error("500 Ошибка: {}", e.getMessage());
        return new ResponseEntity<>(
                Map.of("error", "Ошибка.",
                        "errorMessage", e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundException(final NotFoundException e) {
        log.error("404 Не найден id: {}", e.getMessage());
        return new ResponseEntity<>(
                Map.of("error", "Не найден id.",
                        "errorMessage", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

}
