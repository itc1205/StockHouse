package com.itc.StockHouse.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.itc.StockHouse.utils.criteriamapping.strategies.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "field", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CriteriaDTO.NameCriteria.class, name = "name"),
        @JsonSubTypes.Type(value = CriteriaDTO.PriceCriteria.class, name = "price"),
        @JsonSubTypes.Type(value = CriteriaDTO.AmountCriteria.class, name = "amount"),
        @JsonSubTypes.Type(value = CriteriaDTO.CategoryCriteria.class, name = "category"),
        @JsonSubTypes.Type(value = CriteriaDTO.VendorCodeCriteria.class, name = "vendorCode"),
        @JsonSubTypes.Type(value = CriteriaDTO.DescriptionCriteria.class, name = "description"),
        @JsonSubTypes.Type(value = CriteriaDTO.UpdateDateCriteria.class, name = "updateDate"),
        @JsonSubTypes.Type(value = CriteriaDTO.CreationDateCriteria.class, name = "creationDate"),
})

@Data
@RequiredArgsConstructor
public class CriteriaDTO<T> {

    @JsonIgnore
    private final PredicateMapperStrategy<T> predicateMapperStrategy;
    @NotNull
    private String field;
    @NotNull
    private OperationDTO op;
    @NotNull
    private T value;

    public <X> Predicate toPredicate(CriteriaBuilder criteriaBuilder, Root<X> root) {
        switch (op) {
            case EQUAL -> {
                return predicateMapperStrategy.toEqualPredicate(value, criteriaBuilder, root.get(field));

            }
            case LIKE -> {
                return predicateMapperStrategy.toLikePredicate(value, criteriaBuilder, root.get(field));

            }
            case GREATER_THAN_OR_EQ -> {
                return predicateMapperStrategy.toGreaterThanOrEqualPredicate(value, criteriaBuilder, root.get(field));

            }
            case LESS_THAN_OR_EQ -> {
                return predicateMapperStrategy.toLessThanOrEqualPredicate(value, criteriaBuilder, root.get(field));
            }
            default -> {
                throw new UnsupportedOperationException();
            }
        }
    }

    public static class NameCriteria extends CriteriaDTO<String> {
        public NameCriteria() {
            super(new StringPredicateMapperStrategy());
        }
    }

    public static class PriceCriteria extends CriteriaDTO<BigDecimal> {
        public PriceCriteria() {
            super(new BigDecimalPredicateMapperStrategy());
        }
    }

    public static class AmountCriteria extends CriteriaDTO<Integer> {
        public AmountCriteria() {
            super(new IntegerPredicateMapperStrategy());
        }
    }

    public static class CategoryCriteria extends CriteriaDTO<String> {
        public CategoryCriteria() {
            super(new StringPredicateMapperStrategy());
        }
    }

    public static class VendorCodeCriteria extends CriteriaDTO<String> {
        public VendorCodeCriteria() {
            super(new StringPredicateMapperStrategy());
        }
    }

    public static class DescriptionCriteria extends CriteriaDTO<String> {
        public DescriptionCriteria() {
            super(new StringPredicateMapperStrategy());
        }
    }

    public static class UpdateDateCriteria extends CriteriaDTO<OffsetDateTime> {
        public UpdateDateCriteria() {
            super(new OffsetDateTimePredicateMapperStrategy());
        }
    }

    public static class CreationDateCriteria extends CriteriaDTO<OffsetDateTime> {
        public CreationDateCriteria() {
            super(new OffsetDateTimePredicateMapperStrategy());
        }
    }
}