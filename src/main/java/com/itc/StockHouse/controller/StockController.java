package com.itc.StockHouse.controller;

import com.itc.StockHouse.dto.CreateStockDto;
import com.itc.StockHouse.dto.PatchAmountStockDTO;
import com.itc.StockHouse.dto.StockDto;
import com.itc.StockHouse.dto.UpdateStockDto;
import com.itc.StockHouse.exceptions.StockVendorCodeAlreadyExistsException;
import com.itc.StockHouse.exceptions.StockNotFoundException;
import com.itc.StockHouse.service.StockService;
import com.itc.StockHouse.utils.StockMappingUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Rest-контроллер для сервиса по управлению товарами на складе
 *
 * <p> Основная задача контроллера - маппинг сервиса {@link StockService} к соответствующим конечным точкам приложения</p>
 */
@RestController
@Validated
@RequestMapping("/api/v1/stock")
public class StockController {
    @Autowired
    private StockService stockService;

    @Autowired
    private StockMappingUtils utils;

    @Operation(summary = "Операция создания товара на складе")
    @PostMapping("/")
    public StockDto createStock(@RequestBody CreateStockDto createStockDto) throws StockVendorCodeAlreadyExistsException {
        return utils.mapToStockDto(
                stockService.createStock(
                        utils.mapToStockEntity(createStockDto)
                )
        );
    }

    @Operation(summary = "Операция получения информации о товаре на складе")
    @GetMapping("/{id}")
    public StockDto getStockByID(@PathVariable("id") UUID id) throws StockNotFoundException {
        return utils.mapToStockDto(
                stockService.getStockByUUID(id)
        );
    }

    @Operation(summary = "Операция получения всех товаров на складе")
    @GetMapping("/")
    public List<StockDto> getAllStocks(Pageable pageable) {
        return stockService.getAll(pageable)
                .stream()
                .map(utils::mapToStockDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Операция обновления товара на складе")
    @PutMapping("/")
    public StockDto updateStock(@RequestBody UpdateStockDto stockDto) throws StockVendorCodeAlreadyExistsException, StockNotFoundException {
        return utils.mapToStockDto(
                stockService.updateStock(
                        utils.mapToStockEntity(stockDto)
                )
        );
    }

    @Operation(summary = "Операция обновления количества товара на складе")
    @PatchMapping("/{id}")
    public StockDto updateStockAmount(@PathVariable("id") UUID id,
                                      @RequestBody PatchAmountStockDTO amountStockDTO) {
        return utils.mapToStockDto(
                stockService.updateAmountOfStock(id, amountStockDTO.getAmount())
        );
    }

    @Operation(summary = "Операция удаления товара на складе")
    @DeleteMapping("/{id}")
    public void deleteStockByUUID(@PathVariable("id") UUID id) throws StockNotFoundException {
        stockService.deleteStockByUUID(id);
    }
}
