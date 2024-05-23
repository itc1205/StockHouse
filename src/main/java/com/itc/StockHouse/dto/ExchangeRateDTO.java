package com.itc.StockHouse.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeRateDTO {
    BigDecimal CNY;
    BigDecimal EUR;
    BigDecimal USD;
}
