package com.itc.StockHouse.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ValidationErrorDTO {
    private LocalDateTime timestamp;
    private Map<String, String> validationErrors;
}
