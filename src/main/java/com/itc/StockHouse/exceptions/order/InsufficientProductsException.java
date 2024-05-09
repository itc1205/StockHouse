package com.itc.StockHouse.exceptions.order;

import java.util.Map;
import java.util.UUID;

public class InsufficientProductsException extends Throwable {
    private final Map<UUID, Integer> ids;

    public InsufficientProductsException(Map<UUID, Integer> ids) {
        this.ids = ids;
    }
}
