package com.itc.StockHouse.utils;

import com.itc.StockHouse.dto.CreateProductDto;
import com.itc.StockHouse.dto.ProductDto;
import com.itc.StockHouse.dto.UpdateProductDto;
import com.itc.StockHouse.model.ProductEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

/**
 * Утилиты по маппингу DTO объектов к Entity объектам и наоборот
 */
@Service
public class ProductMappingUtils {
    /**
     * Метод для маппинга Entity объекта к DTO
     *
     * @param productEntity Entity объект для маппинга к DTO
     * @return DTO объект, замапленный от Entity
     */
    public ProductDto mapToProductDto(@NotNull ProductEntity productEntity) {
        return ProductDto.builder()
                .id(productEntity.getId())
                .category(productEntity.getCategory())
                .name(productEntity.getName())
                .vendorCode(productEntity.getVendorCode())
                .description(productEntity.getDescription())
                .creationDate(productEntity.getCreationDate())
                .updateDate(productEntity.getUpdateDate())
                .price(productEntity.getPrice())
                .amount(productEntity.getAmount())
                .build();
    }

    /**
     * Метод для маппинга DTO объекта к Entity
     *
     * @param createProductDto DTO-объект для маппинга к Entity
     * @return Entity объект, замапленный от DTO
     */
    public ProductEntity mapToProductEntity(@NotNull CreateProductDto createProductDto) {
        return ProductEntity.builder()
                .category(createProductDto.getCategory())
                .name(createProductDto.getName())
                .vendorCode(createProductDto.getVendorCode())
                .description(createProductDto.getDescription())
                .price(createProductDto.getPrice())
                .amount(createProductDto.getAmount())
                .build();
    }

    /**
     * Метод для маппинга DTO объекта к Entity
     *
     * @param updateProductDto DTO-объект для маппинга к Entity
     * @return Entity объект, замапленный от DTO
     */
    public ProductEntity mapToProductEntity(@NotNull UpdateProductDto updateProductDto) {
        return ProductEntity.builder()
                .id(updateProductDto.getId())
                .category(updateProductDto.getCategory())
                .name(updateProductDto.getName())
                .vendorCode(updateProductDto.getVendorCode())
                .description(updateProductDto.getDescription())
                .price(updateProductDto.getPrice())
                .amount(updateProductDto.getAmount())
                .build();
    }
}
