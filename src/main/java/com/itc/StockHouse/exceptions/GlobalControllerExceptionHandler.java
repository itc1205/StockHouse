package com.itc.StockHouse.exceptions;

import com.itc.StockHouse.dto.ErrorDTO;
import com.itc.StockHouse.dto.ValidationErrorDTO;
import com.itc.StockHouse.exceptions.customer.CustomerAlreadyExistsException;
import com.itc.StockHouse.exceptions.customer.CustomerNotFoundException;
import com.itc.StockHouse.exceptions.order.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
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
     * Обработчик исключений {@link ProductVendorCodeAlreadyExistsException}
     */
    @ExceptionHandler(ProductVendorCodeAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleArticleConflict(ProductVendorCodeAlreadyExistsException ex) {
        return ErrorDTO.builder()
                .timestamp(LocalDateTime.now())
                .error(ex.getMessage())
                .build();
    }
    /**
     * Обработчик исключений {@link ProductNotFoundException}
     */
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleProductNotFound(ProductNotFoundException ex) {
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
    // TODO: Improve exception handling
    @ExceptionHandler(InsufficientRightsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO handleInsufficientRights(InsufficientRightsException ex) {
        return ErrorDTO.builder()
                .timestamp(LocalDateTime.now())
                .error("User does not have rights to access this resource.")
                .build();
    }

    @ExceptionHandler(OrderCantBeCreatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleOrderCantBeCreated(OrderCantBeCreatedException ex) {
        return ErrorDTO.builder()
                .timestamp(LocalDateTime.now())
                .error("Order cannot be created.")
                .build();
    }

    @ExceptionHandler(OrderCantBeDeletedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleOrderCantBeDeleted(OrderCantBeDeletedException ex) {
        return ErrorDTO.builder()
                .timestamp(LocalDateTime.now())
                .error("Order cannot be deleted.")
                .build();
    }

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleOrderNotFound(OrderNotFoundException ex) {
        return ErrorDTO.builder()
                .timestamp(LocalDateTime.now())
                .error("Order not found.")
                .build();
    }

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleCustomerAlreadyExists(CustomerAlreadyExistsException ex) {
        return ErrorDTO.builder()
                .timestamp(LocalDateTime.now())
                .error("Customer with same login already exists.")
                .build();
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleCustomerNotFound(CustomerNotFoundException ex) {
        return ErrorDTO.builder()
                .timestamp(LocalDateTime.now())
                .error("Customer not found.")
                .build();
    }

    @ExceptionHandler(OrderCantBeChangedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleOrderCantBeChanged(OrderCantBeChangedException ex) {
        return ErrorDTO.builder()
                .timestamp(LocalDateTime.now())
                .error("Order cannot be changed.")
                .build();
    }
    @ExceptionHandler(InsufficientProductsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleOrderCantBeChanged(InsufficientProductsException ex) {
        return ErrorDTO.builder()
                .timestamp(LocalDateTime.now())
                .error("Insufficient amount of product.")
                .build();
    }
}
