package com.itc.StockHouse.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ExchangeRateDTO {
    BigDecimal CNY;
    BigDecimal EUR;
    BigDecimal USD;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    ExchangeRateDTO(@JsonProperty("CNY") BigDecimal CNY, @JsonProperty("EUR") BigDecimal EUR, @JsonProperty("USD") BigDecimal USD) {
        this.EUR = EUR;
        this.USD = USD;
        this.CNY = CNY;
    }
}
