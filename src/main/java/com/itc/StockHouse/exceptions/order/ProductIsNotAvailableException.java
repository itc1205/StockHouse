package com.itc.StockHouse.exceptions.order;

import java.util.UUID;

public class ProductIsNotAvailableException extends RuntimeException{
    private final UUID productId;
    public ProductIsNotAvailableException(UUID productId) {
        super();
        this.productId = productId;
    }
}
