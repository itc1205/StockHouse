package com.itc.StockHouse.client.currency;

import com.itc.StockHouse.configurations.CacheConfiguration;
import com.itc.StockHouse.configurations.property.RestProperty;
import com.itc.StockHouse.exceptions.CurrencyServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyServiceClientImpl implements CurrencyServiceClient {

    private final WebClient currencyServiceWebClient;

    private final RestProperty restProperty;

    @Override
    @Cacheable(value = CacheConfiguration.currencyCacheName, unless = "#result == null")
    public HashMap<String, BigDecimal> retrieveCurrencies() {
        return currencyServiceWebClient.get()
                        .uri(getUriFromRestProperty())
                        .retrieve()
                        .onStatus(HttpStatusCode::is5xxServerError,
                                clientResponse -> Mono.error(new CurrencyServiceException("Could not fetch currencies from service. Server returned status code: %s".formatted(clientResponse.statusCode()))))
                        .bodyToMono(new ParameterizedTypeReference<HashMap<String, BigDecimal>>() {})
                        .doOnError(throwable -> {
                            throw new CurrencyServiceException("Could not fetch currencies from service");
                        })
                        .retry(2)
                        .block();
    }

    private String getUriFromRestProperty() {
        return restProperty.getCurrencyService().getEndpoints().get("get-currency");
    }
}
