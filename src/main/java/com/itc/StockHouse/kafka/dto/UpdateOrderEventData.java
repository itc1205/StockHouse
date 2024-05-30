package com.itc.StockHouse.kafka.dto;

import com.itc.StockHouse.dto.schema.order.ProductSchema;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UpdateOrderEventData implements KafkaEvent {
    private String event;
    private UUID orderId;
    private Long customerId;
    private List<ProductSchema> products;
}
