package com.itc.StockHouse.service;

import com.itc.StockHouse.model.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

public interface  SearchService {
    Page<ProductEntity> searchBySpecification(Specification<ProductEntity> specification, PageRequest pageRequest);
}
