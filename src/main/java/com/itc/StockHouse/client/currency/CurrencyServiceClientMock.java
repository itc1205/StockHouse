package com.itc.StockHouse.client.currency;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Random;

@Component
@Slf4j
@Primary
@ConditionalOnProperty(name = "rest.currency-service.mock.enabled", matchIfMissing = false)
public class CurrencyServiceClientMock implements CurrencyServiceClient {
    private static final Random random = new Random();

    private final HashMap<String, BigDecimal> currencies;

    CurrencyServiceClientMock() {
        currencies = new HashMap<>();
        currencies.put("CNY", BigDecimal.valueOf(random.nextFloat(100)));
        currencies.put("USD", BigDecimal.valueOf(random.nextFloat(100)));
        currencies.put("EUR", BigDecimal.valueOf(random.nextFloat(100)));
        log.info("Initialised mock with currencies: {}", currencies);
    }

    @Override
    public HashMap<String, BigDecimal> retrieveCurrencies() {
        return currencies;
    }
}
