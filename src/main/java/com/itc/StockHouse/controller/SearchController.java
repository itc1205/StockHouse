package com.itc.StockHouse.controller;

import com.itc.StockHouse.dto.search.criteria.CriteriaDTO;
import com.itc.StockHouse.dto.ProductDto;
import com.itc.StockHouse.service.SearchService;
import com.itc.StockHouse.utils.ProductMappingUtils;
import com.itc.StockHouse.utils.criteriamapping.CriteriaSpecification;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchController {
    private final SearchService searchService;

    private final ProductMappingUtils stockMapping;

    @PostMapping("/")
    public List<ProductDto> searchByCriteria(

            @RequestBody
            @Valid
            List<CriteriaDTO<?>> criteriaList,

            @RequestParam
            @NotNull(message = "Параметр page не должен быть пустым")
            @Min(value = 0, message = "Номер страницы должен быть положительным")
            Integer page,

            @RequestParam
            @NotNull(message = "Параметр size не должен быть пустым")
            @Min(value = 1, message = "Размер страницы должен быть больше 0")
            Integer size
    ) {
        return searchService
                .searchBySpecification(
                        new CriteriaSpecification(criteriaList),
                        PageRequest.of(page, size)
                )
                .stream()
                .map(stockMapping::mapToProductDto)
                .toList();
    }
}
