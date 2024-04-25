package com.itc.StockHouse.utils.criteriamapping;

import com.itc.StockHouse.dto.CriteriaDTO;
import com.itc.StockHouse.model.StockEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CriteriaMapper {


    public Specification<StockEntity> mapToSpecification(List<CriteriaDTO<?>> criteriaList) {
        return (root, query, criteriaBuilder) -> {
            final List<Predicate> predicates = new ArrayList<>();
            for (CriteriaDTO<?> criteria : criteriaList) {
                predicates.add(criteria.toPredicate(criteriaBuilder, root));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}


