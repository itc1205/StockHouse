package com.itc.StockHouse.dto.search.criteria;

import com.itc.StockHouse.dto.search.OperationDTO;
import com.itc.StockHouse.utils.criteriamapping.strategies.OffsetDateTimePredicateMapperStrategy;
import com.itc.StockHouse.utils.criteriamapping.strategies.PredicateMapperStrategy;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class UpdateDateCriteria implements CriteriaDTO<OffsetDateTime> {
    private final static PredicateMapperStrategy<OffsetDateTime> predicateMapperStrategy = new OffsetDateTimePredicateMapperStrategy();
    private String field;
    private String value;
    private OperationDTO op;

    @Override
    public PredicateMapperStrategy<OffsetDateTime> getPredicateMapperStrategy() {
        return predicateMapperStrategy;
    }
}
