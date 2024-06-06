package com.itc.StockHouse.kafka.dto;

import com.itc.StockHouse.dto.schema.order.ProductSchema;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderEventData implements KafkaEvent {
    private String event;
    private Long customerId;
    String deliveryAddress;
    List<ProductSchema> products;
}
