package com.itc.StockHouse.motherobject;

import lombok.Setter;

import java.util.UUID;

@Setter

public class MotherObject {
    public static ProductBuilder aDefaultStock() {
        return new ProductBuilder();
    }

    public static ProductBuilder aStockWithRandomVendorCode() {
        return new ProductBuilder()
                .withVendorCode(UUID.randomUUID().toString());
    }

    public static ProductBuilder aStockWithRandomName() {
        return new ProductBuilder()
                .withName("Stock#" + UUID.randomUUID());
    }
}


