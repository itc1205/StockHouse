package com.itc.StockHouse.client.currency;

import com.itc.StockHouse.configurations.CacheConfiguration;
import com.itc.StockHouse.exceptions.CurrencyServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
@ConditionalOnExpression(value = "#{'${currency-service.mode:none}'.equals('base')}")
public class CurrencyServiceClientImpl implements CurrencyServiceClient {

    private final WebClient webClient;

    @Value("${currency-service.host}")
    private String host;

    @Value("${currency-service.methods.get-currency}")
    private String getCurrencyEndpoint;


    @Override
    @Cacheable(value = CacheConfiguration.currencyCacheName, unless = "#result == null")
    public HashMap<String, BigDecimal> retrieveCurrencies() {
        return webClient.get()
                        .uri(host + getCurrencyEndpoint)
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
}
