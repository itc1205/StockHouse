package com.itc.StockHouse.service;

import com.itc.StockHouse.model.StockEntity;
import com.itc.StockHouse.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final StockRepository stockRepository;

    @Override
    public Page<StockEntity> searchBySpecification(Specification<StockEntity> specification, PageRequest pageRequest) {
        return stockRepository.findAll(specification, pageRequest);
    }
}
