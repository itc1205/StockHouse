package com.itc.StockHouse.kafka.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "event",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateOrderEventData.class, name = Event.CREATE_ORDER),
        @JsonSubTypes.Type(value = DeleteOrderEventData.class, name = Event.DELETE_ORDER),
        @JsonSubTypes.Type(value = UpdateOrderEventData.class, name = Event.UPDATE_ORDER),
        @JsonSubTypes.Type(value = UpdateOrderStatusEventData.class, name = Event.UPDATE_ORDER_STATUS),
})
public interface KafkaEvent {
    String getEvent();
}