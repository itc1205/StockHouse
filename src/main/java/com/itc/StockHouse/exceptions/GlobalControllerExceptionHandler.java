package com.itc.StockHouse.exceptions;

import com.itc.StockHouse.dto.ErrorDTO;
import com.itc.StockHouse.dto.ValidationErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * Глобальный обработчик исключений
 *
 * <p> Здесь обрабатываются все исключения приложения </p>
 */
@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    /**
     * Обработчик исключений {@link StockVendorCodeAlreadyExistsException}
     */
    @ExceptionHandler(StockVendorCodeAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleArticleConflict(StockVendorCodeAlreadyExistsException ex) {
        return ErrorDTO.builder()
                .timestamp(LocalDateTime.now())
                .error(ex.getMessage())
                .build();
    }
    /**
     * Обработчик исключений {@link com.itc.StockHouse.exceptions.StockNotFoundException}
     */
    @ExceptionHandler(StockNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO NotFound(StockNotFoundException ex) {
        return ErrorDTO.builder()
                .timestamp(LocalDateTime.now())
                .error(ex.getMessage())
                .build();
    }
    /**
     * Обработчик исключений ошибок валидации
     * <p> Собирает все ошибки валидации и отправляет их обратно пользователю </p>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorDTO handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // Collect all validation errors
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        // Convert them into DTO
        return ValidationErrorDTO.builder()
                .timestamp(LocalDateTime.now())
                .validationErrors(errors)
                .build();
    }
}
