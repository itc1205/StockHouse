package com.itc.StockHouse.dto.schema.order;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ProductSchema {
    @NotNull
    UUID productId;
    @NotNull
    Integer quantity;
}
