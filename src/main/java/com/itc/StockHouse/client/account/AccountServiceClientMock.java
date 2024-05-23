package com.itc.StockHouse.client.account;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Primary
public class AccountServiceClientMock implements AccountServiceClient {
    @Override
    public CompletableFuture<Map<String, String>> retrieveAccountNumbers(List<String> logins) {
        return CompletableFuture.supplyAsync(
                () -> {
                    long startTime = System.currentTimeMillis();
                    Map<String, String> inns = logins.stream().collect(Collectors.toMap(
                            Function.identity(), login -> "AccountNumber#" + login
                    ));
                    try {
                        Thread.sleep(Duration.ofSeconds(3).toMillis() - (System.currentTimeMillis() - startTime));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return inns;
                }
        );
    }
}
