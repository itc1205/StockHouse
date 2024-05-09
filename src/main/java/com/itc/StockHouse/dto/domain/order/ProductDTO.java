package com.itc.StockHouse.dto.domain.order;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class ProductDTO {
    UUID productId;
    String name;
    Integer quantity;
    BigDecimal price;
}
