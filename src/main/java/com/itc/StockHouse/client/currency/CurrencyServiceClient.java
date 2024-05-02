package com.itc.StockHouse.client.currency;

import java.math.BigDecimal;
import java.util.HashMap;

public interface CurrencyServiceClient {
    public HashMap<String, BigDecimal> retrieveCurrencies();
}
