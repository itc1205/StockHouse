package com.itc.StockHouse.dto.search.criteria;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.itc.StockHouse.dto.search.OperationDTO;
import com.itc.StockHouse.utils.criteriamapping.strategies.PredicateMapperStrategy;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "field", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = NameCriteria.class, name = "name"),
        @JsonSubTypes.Type(value = PriceCriteria.class, name = "price"),
        @JsonSubTypes.Type(value = AmountCriteria.class, name = "amount"),
        @JsonSubTypes.Type(value = CategoryCriteria.class, name = "category"),
        @JsonSubTypes.Type(value = VendorCodeCriteria.class, name = "vendorCode"),
        @JsonSubTypes.Type(value = DescriptionCriteria.class, name = "description"),
        @JsonSubTypes.Type(value = UpdateDateCriteria.class, name = "updateDate"),
        @JsonSubTypes.Type(value = CreationDateCriteria.class, name = "creationDate"),
})
public interface CriteriaDTO<T> {
    PredicateMapperStrategy<T> getPredicateMapperStrategy();
    String getField();
    OperationDTO getOp();
    T getValue();
}