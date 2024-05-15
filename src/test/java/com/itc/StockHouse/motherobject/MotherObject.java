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

    public static CustomerBuilder aCustomerWithRandomLogin() {
        return new CustomerBuilder().withLogin("Login#" + UUID.randomUUID());
    }

    public static ProductBuilder aStockWithRandomName() {
        return new ProductBuilder()
                .withName("Stock#" + UUID.randomUUID());
    }

    public static OrderBuilder aDefaultOrder() {
        return new OrderBuilder();
    }
}


