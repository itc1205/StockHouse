package com.itc.StockHouse.dto.schema.order;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Data
public class ProductSchema {
    @NotNull
    UUID productId;
    @NotNull
    Integer quantity;
}
