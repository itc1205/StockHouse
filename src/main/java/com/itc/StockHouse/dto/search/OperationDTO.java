package com.itc.StockHouse.dto.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OperationDTO {
    EQUAL("="),
    LIKE("~"),
    GREATER_THAN_OR_EQ(">="),
    LESS_THAN_OR_EQ("<=");

    private final String shortenedOperatorName;

    @JsonCreator
    public static OperationDTO fromString(@JsonProperty String name) throws JsonParseException {
        for (OperationDTO op : OperationDTO.values()) {
            if (op.shortenedOperatorName.equals(name) | op.name().equals(name)) {
                return op;
            }
        }
        throw new JsonParseException("Operation %s is unsupported".formatted(name));
    }
}
