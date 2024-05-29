package com.itc.StockHouse.dto.schema.order;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Data
public class CreateOrderSchema {
    @NotNull
    String deliveryAddress;
    @NotNull
    List<ProductSchema> products;
}
