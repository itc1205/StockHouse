package com.itc.StockHouse.kafka.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class DeleteOrderEventData implements KafkaEvent {
    private String event;
    private UUID orderId;
    private Long customerId;
}
