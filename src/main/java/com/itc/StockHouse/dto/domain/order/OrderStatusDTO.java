package com.itc.StockHouse.dto.domain.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public enum OrderStatusDTO {
    CREATED,
    CONFIRMED,
    CANCELLED,
    DONE,
    REJECTED;

    @JsonValue
    public String getCode() {
        return this.name();
    }

    @JsonCreator
    public static OrderStatusDTO fromCode(@JsonProperty String code) {
        for (OrderStatusDTO operationType : OrderStatusDTO.values()) {
            if (operationType.name().equals(code)) {
                return operationType;
            }
        }
        return null;
    }
}
