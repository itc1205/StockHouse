package com.itc.StockHouse.kafka.handlers;

import com.itc.StockHouse.kafka.dto.KafkaEvent;

public interface KafkaEventHandler<T extends KafkaEvent> {
    void handle(T event);
}
