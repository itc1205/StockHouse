package com.itc.StockHouse.dto.search.criteria;

import com.itc.StockHouse.dto.search.OperationDTO;
import com.itc.StockHouse.utils.criteriamapping.strategies.IntegerPredicateMapperStrategy;
import com.itc.StockHouse.utils.criteriamapping.strategies.PredicateMapperStrategy;
import lombok.Data;

@Data
public class AmountCriteria implements CriteriaDTO<Integer> {
    private static final PredicateMapperStrategy<Integer> predicateMapperStrategy = new IntegerPredicateMapperStrategy();
    private String field;
    private Integer value;
    private OperationDTO op;

    @Override
    public PredicateMapperStrategy<Integer> getPredicateMapperStrategy() {
        return predicateMapperStrategy;
    }
}
