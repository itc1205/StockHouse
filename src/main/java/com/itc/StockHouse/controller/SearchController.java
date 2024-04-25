package com.itc.StockHouse.controller;

import com.itc.StockHouse.dto.CriteriaDTO;
import com.itc.StockHouse.dto.StockDto;
import com.itc.StockHouse.service.SearchService;
import com.itc.StockHouse.utils.StockMappingUtils;
import com.itc.StockHouse.utils.criteriamapping.CriteriaMapper;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchController {
    private final SearchService searchService;

    private final StockMappingUtils stockMapping;

    private final CriteriaMapper criteriaMapping;

    @PostMapping("/")
    public List<StockDto> searchByCriteria(

            @RequestBody
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
                        criteriaMapping.mapToSpecification(criteriaList),
                        PageRequest.of(page, size)
                )
                .stream()
                .map(stockMapping::mapToStockDto)
                .toList();
    }
}
