package com.itc.StockHouse.motherobject;

import lombok.Setter;

import java.util.UUID;

@Setter

public class MotherObject {
    public static StockBuilder aDefaultStock() {
        return new StockBuilder();
    }

    public static StockBuilder aStockWithRandomVendorCode() {
        return new StockBuilder()
                .withVendorCode(UUID.randomUUID().toString());
    }

    public static StockBuilder aStockWithRandomName() {
        return new StockBuilder()
                .withName("Stock#" + UUID.randomUUID());
    }
}


