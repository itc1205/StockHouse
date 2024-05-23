package com.itc.StockHouse.client.crm;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnProperty(name = "rest.crm-service.mock.enabled", matchIfMissing = false)
public class CrmServiceClientMock implements CrmServiceClient {
    @Override
    public CompletableFuture<Map<String, String>> retrieveInns(List<String> logins) {
        return CompletableFuture.supplyAsync(
                () -> {
                    long startTime = System.currentTimeMillis();
                    Map<String, String> inns = logins.stream().collect(Collectors.toMap(
                            Function.identity(), login -> "INN#" + login
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
