package com.itc.StockHouse.client.currency;

import com.itc.StockHouse.dto.ExchangeRateDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Random;

@Component
@Slf4j
@Primary
@ConditionalOnProperty(name = "rest.currency-service.mock.enabled", matchIfMissing = false)
public class CurrencyServiceClientMock implements CurrencyServiceClient {
    private static final Random random = new Random();

    private final ExchangeRateDTO exchangeRates = new ExchangeRateDTO();

    CurrencyServiceClientMock() {
        exchangeRates.setCNY(BigDecimal.valueOf(20));
        exchangeRates.setUSD(BigDecimal.valueOf(45));
        exchangeRates.setEUR(BigDecimal.valueOf(100));
        log.info("Initialised mock with currencies: {}", exchangeRates);
    }

    @Override
    public ExchangeRateDTO retrieveCurrencies() {
        return exchangeRates;
    }
}
