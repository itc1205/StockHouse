package com.itc.StockHouse.dto.search.criteria;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itc.StockHouse.dto.search.OperationDTO;
import com.itc.StockHouse.utils.criteriamapping.strategies.OffsetDateTimePredicateMapperStrategy;
import com.itc.StockHouse.utils.criteriamapping.strategies.PredicateMapperStrategy;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class UpdateDateCriteria implements CriteriaDTO<OffsetDateTime> {
    @JsonIgnore
    private final static PredicateMapperStrategy<OffsetDateTime> predicateMapperStrategy = new OffsetDateTimePredicateMapperStrategy();
    @NotNull
    private String field;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime value;
    @NotNull
    private OperationDTO op;

    @Override
    public PredicateMapperStrategy<OffsetDateTime> getPredicateMapperStrategy() {
        return predicateMapperStrategy;
    }
}
