package com.itc.StockHouse.utils.criteriamapping.strategies;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

public class StringPredicateMapperStrategy implements PredicateMapperStrategy<String> {

    @Override
    public Predicate toEqualPredicate(String value, CriteriaBuilder cb, Expression<String> expr) {
        return cb.equal(expr, value);
    }

    @Override
    public Predicate toLikePredicate(String value, CriteriaBuilder cb, Expression<String> expr) {
        return cb.like(expr, "%" + value + "%");
    }

    @Override
    public Predicate toGreaterThanOrEqualPredicate(String value, CriteriaBuilder cb, Expression<String> expr) {
        return cb.like(expr, value + "%");
    }

    @Override
    public Predicate toLessThanOrEqualPredicate(String value, CriteriaBuilder cb, Expression<String> expr) {
        return cb.like(expr, "%" + value);
    }
}
