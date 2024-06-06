package com.itc.StockHouse.configurations;

import com.itc.StockHouse.configurations.property.RestProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfiguration {
    private final RestProperty restProperty;

    @Bean
    public WebClient currencyServiceWebClient() {
        return WebClient.builder()
                .baseUrl(restProperty.getCurrencyService().getHost())
                .build();
    }
    @Bean
    public WebClient accountServiceWebClient() {
        return WebClient.builder()
                .baseUrl(restProperty.getAccountService().getHost())
                .build();
    }
    @Bean
    public WebClient crmServiceWebClient() {
        return WebClient.builder()
                .baseUrl(restProperty.getCrmService().getHost())
                .build();
    }
}