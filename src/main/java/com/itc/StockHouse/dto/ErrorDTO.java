package com.itc.StockHouse.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO-объект содержащий информацию об ошибке
 */
@Data
@Builder
public class ErrorDTO {
    /**
     * Временная метка ошибки
     */
    private LocalDateTime timestamp;
    /**
     * Информация об ошибке
     */
    private String error;
}
