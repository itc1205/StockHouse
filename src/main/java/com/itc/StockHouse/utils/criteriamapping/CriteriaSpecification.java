package com.itc.StockHouse.utils.criteriamapping;

import com.itc.StockHouse.dto.search.criteria.CriteriaDTO;
import com.itc.StockHouse.model.ProductEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@SuppressWarnings({"unchecked", "rawtypes"})
@RequiredArgsConstructor
public class CriteriaSpecification implements Specification<ProductEntity> {

    private final List<CriteriaDTO<?>> criteriaList;

    @Override
    public Predicate toPredicate(@NotNull Root<ProductEntity> root, @NotNull CriteriaQuery<?> query, @NotNull CriteriaBuilder criteriaBuilder) {
        final List<Predicate> predicates = new ArrayList<>();
        for (CriteriaDTO criteria : criteriaList) {
            switch (criteria.getOp()) {
                case EQUAL -> predicates.add(criteria.getPredicateMapperStrategy()
                        .toEqualPredicate(
                                criteria.getValue(),
                                criteriaBuilder,
                                root.get(criteria.getField())
                        )
                );
                case LIKE -> predicates.add(criteria.getPredicateMapperStrategy()
                        .toLikePredicate(
                                criteria.getValue(),
                                criteriaBuilder,
                                root.get(criteria.getField())
                        )
                );
                case GREATER_THAN_OR_EQ -> predicates.add(criteria.getPredicateMapperStrategy()
                        .toGreaterThanOrEqualPredicate(
                                criteria.getValue(),
                                criteriaBuilder,
                                root.get(criteria.getField())
                        )
                );
                case LESS_THAN_OR_EQ -> predicates.add(criteria.getPredicateMapperStrategy().
                        toLessThanOrEqualPredicate(
                                criteria.getValue(),
                                criteriaBuilder,
                                root.get(criteria.getField())
                        )
                );
                default -> throw new UnsupportedOperationException();
            }
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}


