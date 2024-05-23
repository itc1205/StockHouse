package com.itc.StockHouse.support.currency;

import com.itc.StockHouse.client.currency.CurrencyServiceClient;
import com.itc.StockHouse.dto.StockDto;
import com.itc.StockHouse.exceptions.CurrencyServiceException;
import com.itc.StockHouse.utils.CurrencyInputStreamMappingUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyProvider {
    @Value("classpath:exchange-rate.json")
    private Resource currenciesResource;

    private final CurrencyServiceClient currencyServiceClient;
    private final CurrencyInputStreamMappingUtil util;

    @SneakyThrows
    public StockDto appendCurrency(StockDto stockDto, String currencyName) {
        // Get currencies from service or else fallback to file
        HashMap<String, BigDecimal> currencies = null;
        try {
            currencies = currencyServiceClient.retrieveCurrencies();
            log.debug("Fetched data from service, got: {}", currencies);
        } catch (CurrencyServiceException ex) {
            log.debug("Failed to fetch data from service with exception: {}", ex.toString());
            log.debug("Using exchange-rate.json instead");
        }

        if (currencies == null) {
            currencies = util.mapFromStream(currenciesResource.getInputStream());
            log.debug("Fetched data from file, got: {}", currencies);
        }

        if (!currencies.containsKey(currencyName)) {
            stockDto.setCurrency("RUB");
            return stockDto;
        }

        BigDecimal currency = currencies.get(currencyName);
        BigDecimal newPrice = stockDto.getPrice().multiply(currency);

        stockDto.setPrice(newPrice);
        stockDto.setCurrency(currencyName);

        return stockDto;
    }

}
