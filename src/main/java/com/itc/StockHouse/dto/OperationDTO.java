package com.itc.StockHouse.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public enum OperationDTO {
    EQUAL(Arrays.asList("=", "EQUAL")),
    LIKE(Arrays.asList("~", "LIKE")),
    GREATER_THAN_OR_EQ(Arrays.asList(">=", "GREATER_THAN_OR_EQ")),
    LESS_THAN_OR_EQ(Arrays.asList("<=", "LESS_THAN_OR_EQ"));

    private final List<String> allowedStrings;

    @JsonCreator
    public static OperationDTO fromString(@JsonProperty String name) throws JsonParseException {
        for (OperationDTO op : OperationDTO.values()) {
            if (op.allowedStrings.contains(name)) {
                return op;
            }
        }
        throw new JsonParseException("Operation %s is unsupported".formatted(name));
    }
}
