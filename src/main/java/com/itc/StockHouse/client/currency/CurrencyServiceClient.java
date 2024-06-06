package com.itc.StockHouse.client.currency;

import com.itc.StockHouse.dto.ExchangeRateDTO;

import java.math.BigDecimal;
import java.util.HashMap;

public interface CurrencyServiceClient {
    public ExchangeRateDTO retrieveCurrencies();
}
