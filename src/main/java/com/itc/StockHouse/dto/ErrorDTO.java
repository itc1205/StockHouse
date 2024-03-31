package com.itc.StockHouse.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class ErrorDTO {
    private LocalDateTime timestamp;
    private String error;
}
