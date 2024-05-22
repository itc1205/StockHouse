package com.itc.StockHouse.exceptions.order;

import java.util.Set;
import java.util.UUID;

public class ProductsNotFoundException extends RuntimeException {
    Set<UUID> missingIds;

    public ProductsNotFoundException(Set<UUID> missingIds) {
    }
}
