package com.itc.StockHouse.dto.search.criteria;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itc.StockHouse.dto.search.OperationDTO;
import com.itc.StockHouse.utils.criteriamapping.strategies.IntegerPredicateMapperStrategy;
import com.itc.StockHouse.utils.criteriamapping.strategies.PredicateMapperStrategy;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AmountCriteria implements CriteriaDTO<Integer> {
    @JsonIgnore
    private static final PredicateMapperStrategy<Integer> predicateMapperStrategy = new IntegerPredicateMapperStrategy();
    @NotNull
    private String field;
    @NotNull
    private Integer value;
    @NotNull
    private OperationDTO op;

    @Override
    public PredicateMapperStrategy<Integer> getPredicateMapperStrategy() {
        return predicateMapperStrategy;
    }
}
