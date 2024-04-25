package com.itc.StockHouse.utils.criteriamapping.strategies;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import java.math.BigDecimal;

public class BigDecimalPredicateMapperStrategy implements PredicateMapperStrategy<BigDecimal> {

    @Override
    public Predicate toEqualPredicate(BigDecimal value, CriteriaBuilder cb, Expression<BigDecimal> expr) {
        return cb.equal(expr, value);
    }

    @Override
    public Predicate toLikePredicate(BigDecimal value, CriteriaBuilder cb, Expression<BigDecimal> expr) {
        final BigDecimal RANGE = BigDecimal.TEN;
        return cb.and(
                cb.lessThanOrEqualTo(expr, value.add(RANGE)),
                cb.greaterThanOrEqualTo(expr, value.subtract(RANGE))
        );
    }

    @Override
    public Predicate toGreaterThanOrEqualPredicate(BigDecimal value, CriteriaBuilder cb, Expression<BigDecimal> expr) {
        return cb.greaterThanOrEqualTo(expr, value);
    }

    @Override
    public Predicate toLessThanOrEqualPredicate(BigDecimal value, CriteriaBuilder cb, Expression<BigDecimal> expr) {
        return cb.lessThanOrEqualTo(expr, value);
    }
}
