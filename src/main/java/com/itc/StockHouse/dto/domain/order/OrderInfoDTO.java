package com.itc.StockHouse.dto.domain.order;

import com.itc.StockHouse.dto.domain.customer.CustomerInfoDTO;
import com.itc.StockHouse.model.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OrderInfoDTO {
    UUID id;
    CustomerInfoDTO customer;
    OrderStatus status;
    String deliveryAddress;
    Integer quantity;
}
