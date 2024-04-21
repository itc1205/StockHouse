package com.itc.StockHouse.utils;

import com.itc.StockHouse.dto.CreateStockDto;
import com.itc.StockHouse.dto.StockDto;
import com.itc.StockHouse.dto.UpdateStockDto;
import com.itc.StockHouse.model.StockEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

/**
 * Утилиты по маппингу DTO объектов к Entity объектам и наоборот
 */
@Service
public class StockMappingUtils {
    /**
     * Метод для маппинга Entity объекта к DTO
     *
     * @param stockEntity Entity объект для маппинга к DTO
     * @return DTO объект, замапленный от Entity
     */
    public StockDto mapToStockDto(@NotNull StockEntity stockEntity) {
        return StockDto.builder()
                .id(stockEntity.getId())
                .category(stockEntity.getCategory())
                .name(stockEntity.getName())
                .vendorCode(stockEntity.getVendorCode())
                .description(stockEntity.getDescription())
                .creationDate(stockEntity.getCreationDate())
                .updateDate(stockEntity.getUpdateDate())
                .price(stockEntity.getPrice())
                .amount(stockEntity.getAmount())
                .build();
    }

    /**
     * Метод для маппинга DTO объекта к Entity
     *
     * @param stockDto DTO-объект для маппинга к Entity
     * @return Entity объект, замапленный от DTO
     */
    public StockEntity mapToStockEntity(@NotNull CreateStockDto stockDto) {
        return StockEntity.builder()
                .category(stockDto.getCategory())
                .name(stockDto.getName())
                .vendorCode(stockDto.getVendorCode())
                .description(stockDto.getDescription())
                .price(stockDto.getPrice())
                .amount(stockDto.getAmount())
                .build();
    }

    /**
     * Метод для маппинга DTO объекта к Entity
     *
     * @param stockDto DTO-объект для маппинга к Entity
     * @return Entity объект, замапленный от DTO
     */
    public StockEntity mapToStockEntity(@NotNull UpdateStockDto stockDto) {
        return StockEntity.builder()
                .id(stockDto.getId())
                .category(stockDto.getCategory())
                .name(stockDto.getName())
                .vendorCode(stockDto.getVendorCode())
                .description(stockDto.getDescription())
                .price(stockDto.getPrice())
                .amount(stockDto.getAmount())
                .build();
    }
}
