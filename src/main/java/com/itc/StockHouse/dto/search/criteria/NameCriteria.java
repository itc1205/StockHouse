package com.itc.StockHouse.dto.search.criteria;

import com.itc.StockHouse.dto.search.OperationDTO;
import com.itc.StockHouse.utils.criteriamapping.strategies.PredicateMapperStrategy;
import com.itc.StockHouse.utils.criteriamapping.strategies.StringPredicateMapperStrategy;


import lombok.Data;

@Data
public class NameCriteria implements CriteriaDTO<String> {
    private final static PredicateMapperStrategy<String> predicateMapperStrategy = new StringPredicateMapperStrategy();
    private String field;
    private String value;
    private OperationDTO op;

    @Override
    public PredicateMapperStrategy<String> getPredicateMapperStrategy() {
        return predicateMapperStrategy;
    }
}
