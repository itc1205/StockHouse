package com.itc.StockHouse.utils.criteriamapping.strategies;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAmount;

public class OffsetDateTimePredicateMapperStrategy implements PredicateMapperStrategy<OffsetDateTime> {

    @Override
    public Predicate toEqualPredicate(OffsetDateTime value, CriteriaBuilder cb, Expression<OffsetDateTime> expr) {
        return cb.equal(expr, value.toLocalDate());
    }

    @Override
    public Predicate toLikePredicate(OffsetDateTime value, CriteriaBuilder cb, Expression<OffsetDateTime> expr) {
        TemporalAmount RANGE = Duration.ofDays(10);
        return cb.and(
                cb.lessThanOrEqualTo(expr, value.minus(RANGE)),
                cb.greaterThanOrEqualTo(expr, value.plus(RANGE))
        );
    }

    @Override
    public Predicate toGreaterThanOrEqualPredicate(OffsetDateTime value, CriteriaBuilder cb, Expression<OffsetDateTime> expr) {
        return cb.greaterThanOrEqualTo(expr, value);
    }

    @Override
    public Predicate toLessThanOrEqualPredicate(OffsetDateTime value, CriteriaBuilder cb, Expression<OffsetDateTime> expr) {
        return cb.lessThanOrEqualTo(expr, value);
    }
}
