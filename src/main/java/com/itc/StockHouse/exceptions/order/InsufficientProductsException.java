package com.itc.StockHouse.exceptions.order;

import java.util.Map;
import java.util.UUID;

public class InsufficientProductsException extends RuntimeException {
    private final UUID productId;
    private final Integer amount;

    public InsufficientProductsException(UUID productId, Integer amount) {
        this.productId = productId;
        this.amount = amount;
    }
}
