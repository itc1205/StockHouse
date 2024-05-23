package com.itc.StockHouse.client.account;

import com.itc.StockHouse.configurations.property.RestProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class AccountServiceClientImpl implements AccountServiceClient {

    private final RestProperty restProperty;
    private final WebClient accountServiceWebClient;

    @Override
    public CompletableFuture<Map<String, String>> retrieveAccountNumbers(List<String> logins) {
        long startTime = System.currentTimeMillis();

        return accountServiceWebClient.get()
                .uri(getUriFromRestProperty())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .toFuture();
    }

    private String getUriFromRestProperty() {
        return restProperty.getAccountService().getMethods().get("get-account-numbers");
    }
}
