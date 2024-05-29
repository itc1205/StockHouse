package com.itc.StockHouse.dto.domain.order;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class OrderDTO {
    UUID orderId;
    String deliveryAddress;
    List<ProductDTO> products;
    BigDecimal totalPrice;
}
