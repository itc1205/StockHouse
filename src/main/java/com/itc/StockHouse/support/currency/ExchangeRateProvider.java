package com.itc.StockHouse.support.currency;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.itc.StockHouse.client.currency.CurrencyServiceClient;
import com.itc.StockHouse.dto.ExchangeRateDTO;
import com.itc.StockHouse.exceptions.CurrencyServiceException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateProvider {
    private static final JsonMapper objectMapper = new JsonMapper();

    private final CurrencyServiceClient currencyServiceClient;

    @Value("classpath:exchange-rate.json")
    private Resource exchangeRatesResource;

    public BigDecimal getExchangeRate(Currency currency) {
        return Optional.ofNullable(getExchangeRateFromService(currency))
                .orElseGet(() -> getExchangeRateFromFile(currency));
    }

    @SneakyThrows
    private BigDecimal getExchangeRateFromFile(Currency currency) {
        ExchangeRateDTO exchangeRateDTO = objectMapper.readValue(exchangeRatesResource.getFile(), ExchangeRateDTO.class);
        return getExchangeRateByCurrency(exchangeRateDTO, currency);
    }

    private @Nullable BigDecimal getExchangeRateFromService(Currency currency) {
        try {
            ExchangeRateDTO exchangeRateDTO = currencyServiceClient.retrieveCurrencies();
            return getExchangeRateByCurrency(exchangeRateDTO, currency);
        } catch (CurrencyServiceException ex) {
            log.warn(ex.getMessage());
        }
        return null;
    }
    private BigDecimal getExchangeRateByCurrency(ExchangeRateDTO exchangeRate, Currency currency) {
        return switch (currency) {
            case USD -> exchangeRate.getUSD();
            case CNY -> exchangeRate.getCNY();
            case EUR -> exchangeRate.getEUR();
            case RUB -> BigDecimal.ONE;
        };
    }
}
