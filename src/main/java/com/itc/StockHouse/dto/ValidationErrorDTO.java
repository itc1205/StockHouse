package com.itc.StockHouse.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO-объект содержащий информацию об ошибках валидации
 */
@Data
@Builder
public class ValidationErrorDTO {
    /**
     * Временная метка ошибки
     */
    private LocalDateTime timestamp;
    /**
     * Ошибки валидации
     */
    private Map<String, String> validationErrors;
}
