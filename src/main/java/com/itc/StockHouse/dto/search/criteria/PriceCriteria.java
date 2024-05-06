package com.itc.StockHouse.dto.search.criteria;

import com.itc.StockHouse.dto.search.OperationDTO;
import com.itc.StockHouse.utils.criteriamapping.strategies.BigDecimalPredicateMapperStrategy;
import com.itc.StockHouse.utils.criteriamapping.strategies.PredicateMapperStrategy;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PriceCriteria implements CriteriaDTO<BigDecimal> {
    private final static PredicateMapperStrategy<BigDecimal> predicateMapperStrategy = new BigDecimalPredicateMapperStrategy();
    private String field;
    private String value;
    private OperationDTO op;

    @Override
    public PredicateMapperStrategy<BigDecimal> getPredicateMapperStrategy() {
        return predicateMapperStrategy;
    }
}
