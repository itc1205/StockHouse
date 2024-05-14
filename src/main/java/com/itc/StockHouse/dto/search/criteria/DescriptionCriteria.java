package com.itc.StockHouse.dto.search.criteria;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itc.StockHouse.dto.search.OperationDTO;
import com.itc.StockHouse.utils.criteriamapping.strategies.PredicateMapperStrategy;
import com.itc.StockHouse.utils.criteriamapping.strategies.StringPredicateMapperStrategy;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DescriptionCriteria implements CriteriaDTO<String> {
    @JsonIgnore
    private final static PredicateMapperStrategy<String> predicateMapperStrategy = new StringPredicateMapperStrategy();
    @NotNull
    private String field;
    @NotNull
    private String value;
    @NotNull
    private OperationDTO op;

    @Override
    public PredicateMapperStrategy<String> getPredicateMapperStrategy() {
        return predicateMapperStrategy;
    }
}
