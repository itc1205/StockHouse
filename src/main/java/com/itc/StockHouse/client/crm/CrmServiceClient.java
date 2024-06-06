package com.itc.StockHouse.client.crm;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface CrmServiceClient {
    CompletableFuture<Map<String, String>> retrieveInns(List<String> logins);
}
