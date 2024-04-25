package com.itc.StockHouse.utils.criteriamapping.strategies;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

public class IntegerPredicateMapperStrategy implements PredicateMapperStrategy<Integer> {
    @Override
    public Predicate toEqualPredicate(Integer value, CriteriaBuilder cb, Expression<Integer> expr) {
        return cb.equal(expr, value);
    }

    @Override
    public Predicate toLikePredicate(Integer value, CriteriaBuilder cb, Expression<Integer> expr) {
        final Integer RANGE = 10;
        return cb.and(
                cb.lessThanOrEqualTo(expr, value + RANGE),
                cb.greaterThanOrEqualTo(expr, value - RANGE)
        );
    }

    @Override
    public Predicate toGreaterThanOrEqualPredicate(Integer value, CriteriaBuilder cb, Expression<Integer> expr) {
        return cb.greaterThanOrEqualTo(expr, value);
    }

    @Override
    public Predicate toLessThanOrEqualPredicate(Integer value, CriteriaBuilder cb, Expression<Integer> expr) {
        return cb.lessThanOrEqualTo(expr, value);
    }
}
