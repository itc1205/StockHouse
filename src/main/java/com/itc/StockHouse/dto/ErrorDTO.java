package com.itc.StockHouse.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorDTO {
    private LocalDateTime timestamp;
    private String error;
}
