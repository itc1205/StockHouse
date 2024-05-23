package com.itc.StockHouse.client.crm;

import com.itc.StockHouse.configurations.property.RestProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class CrmServiceClientImpl implements CrmServiceClient {

    private final RestProperty restProperty;
    private final WebClient crmServiceWebClient;


    @Override
    public CompletableFuture<Map<String, String>> retrieveInns(List<String> logins) {
        long startTime = System.currentTimeMillis();

        return crmServiceWebClient.get()
                .uri(getUriFromRestProperty())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {
                })
                .toFuture();
    }


    private String getUriFromRestProperty() {
        return restProperty.getCrmService().getMethods().get("get-crm");
    }
}
