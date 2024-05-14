package com.itc.StockHouse.service;

import com.itc.StockHouse.model.ProductEntity;
import com.itc.StockHouse.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final ProductRepository productRepository;

    @Override
    public Page<ProductEntity> searchBySpecification(Specification<ProductEntity> specification, Pageable pageable) {
        return productRepository.findAll(specification, pageable);
    }
}
