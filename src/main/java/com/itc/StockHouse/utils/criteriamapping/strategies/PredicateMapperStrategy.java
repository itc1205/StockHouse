package com.itc.StockHouse.utils.criteriamapping.strategies;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

public interface PredicateMapperStrategy<T> {
    Predicate toEqualPredicate(T value, CriteriaBuilder cb, Expression<T> expr);

    Predicate toLikePredicate(T value, CriteriaBuilder cb, Expression<T> expr);

    Predicate toGreaterThanOrEqualPredicate(T value, CriteriaBuilder cb, Expression<T> expr);

    Predicate toLessThanOrEqualPredicate(T value, CriteriaBuilder cb, Expression<T> expr);
}