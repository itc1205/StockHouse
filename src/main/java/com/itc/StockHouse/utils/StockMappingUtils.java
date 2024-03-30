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
        StockDto stockDto = new StockDto();

        stockDto.setUuid(stockEntity.getUuid());
        stockDto.setCategory(stockEntity.getCategory());
        stockDto.setName(stockEntity.getName());
        stockDto.setVendorCode(stockEntity.getVendorCode());
        stockDto.setDescription(stockEntity.getDescription());
        stockDto.setCreationDate(stockEntity.getCreationDate());
        stockDto.setUpdateDate(stockEntity.getUpdateDate());
        stockDto.setPrice(stockEntity.getPrice());
        stockDto.setAmount(stockEntity.getAmount());
        return stockDto;
    }

    /**
     * Метод для маппинга DTO объекта к Entity
     *
     * @param stockDto DTO-объект для маппинга к Entity
     * @return Entity объект, замапленный от DTO
     */
    public StockEntity mapToStockEntity(@NotNull StockDto stockDto) {
        StockEntity stockEntity = new StockEntity();

        stockEntity.setUuid(stockDto.getUuid());
        stockEntity.setCategory(stockDto.getCategory());
        stockEntity.setName(stockDto.getName());
        stockEntity.setVendorCode(stockDto.getVendorCode());
        stockEntity.setDescription(stockDto.getDescription());
        stockEntity.setCreationDate(stockDto.getCreationDate());
        stockEntity.setUpdateDate(stockDto.getUpdateDate());
        stockEntity.setPrice(stockDto.getPrice());
        stockEntity.setAmount(stockDto.getAmount());

        return stockEntity;
    }

    /**
     * Метод для маппинга DTO объекта к Entity
     *
     * @param stockDto DTO-объект для маппинга к Entity
     * @return Entity объект, замапленный от DTO
     */
    public StockEntity mapToStockEntity(@NotNull CreateStockDto stockDto) {
        StockEntity stockEntity = new StockEntity();

        stockEntity.setCategory(stockDto.getCategory());
        stockEntity.setName(stockDto.getName());
        stockEntity.setVendorCode(stockDto.getVendorCode());
        stockEntity.setDescription(stockDto.getDescription());
        stockEntity.setPrice(stockDto.getPrice());
        stockEntity.setAmount(stockDto.getAmount());

        return stockEntity;
    }

    /**
     * Метод для маппинга DTO объекта к Entity
     *
     * @param stockDto DTO-объект для маппинга к Entity
     * @return Entity объект, замапленный от DTO
     */
    public StockEntity mapToStockEntity(@NotNull UpdateStockDto stockDto) {
        StockEntity stockEntity = new StockEntity();

        stockEntity.setUuid(stockDto.getUuid());
        stockEntity.setCategory(stockDto.getCategory());
        stockEntity.setName(stockDto.getName());
        stockEntity.setVendorCode(stockDto.getVendorCode());
        stockEntity.setDescription(stockDto.getDescription());
        stockEntity.setPrice(stockDto.getPrice());
        stockEntity.setAmount(stockDto.getAmount());

        return stockEntity;
    }
}
