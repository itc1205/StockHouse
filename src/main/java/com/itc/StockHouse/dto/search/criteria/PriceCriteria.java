package com.itc.StockHouse.dto.search.criteria;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itc.StockHouse.dto.search.OperationDTO;
import com.itc.StockHouse.utils.criteriamapping.strategies.BigDecimalPredicateMapperStrategy;
import com.itc.StockHouse.utils.criteriamapping.strategies.PredicateMapperStrategy;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PriceCriteria implements CriteriaDTO<BigDecimal> {
    @JsonIgnore
    private final static PredicateMapperStrategy<BigDecimal> predicateMapperStrategy = new BigDecimalPredicateMapperStrategy();
    @NotNull
    private String field;
    @NotNull
    private BigDecimal value;
    @NotNull
    private OperationDTO op;

    @Override
    public PredicateMapperStrategy<BigDecimal> getPredicateMapperStrategy() {
        return predicateMapperStrategy;
    }
}
