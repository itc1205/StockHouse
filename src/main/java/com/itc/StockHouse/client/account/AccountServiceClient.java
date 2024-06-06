package com.itc.StockHouse.client.account;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface AccountServiceClient {
    CompletableFuture<Map<String, String>> retrieveAccountNumbers(List<String> logins);
}
