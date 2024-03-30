package com.itc.StockHouse.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


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
    public ResponseEntity<?> handleArticleConflict(StockVendorCodeAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
    /**
     * Обработчик исключений {@link com.itc.StockHouse.exceptions.StockNotFoundException}
     */
    @ExceptionHandler(StockNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleStockNotFound(StockNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    /**
     * Обработчик исключений ошибок валидации
     * <p> Собирает все ошибки валидации и отправляет их обратно пользователю </p>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
