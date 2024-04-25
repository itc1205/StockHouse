package com.itc.StockHouse.service;

import com.itc.StockHouse.model.StockEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

public interface SearchService {
    Page<StockEntity> searchBySpecification(Specification<StockEntity> specification, PageRequest pageRequest);
}
