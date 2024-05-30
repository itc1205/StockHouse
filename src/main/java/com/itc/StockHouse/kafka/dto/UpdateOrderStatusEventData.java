package com.itc.StockHouse.kafka.dto;

import com.itc.StockHouse.model.OrderStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateOrderStatusEventData implements KafkaEvent {
    private String event;
    private UUID orderId;
    private OrderStatus status;
}
