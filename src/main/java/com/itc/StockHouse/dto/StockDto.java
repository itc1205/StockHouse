package com.itc.StockHouse.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;


/**
 * DTO-объект используемый для передачи информации о товаре клиенту
 */
@Data
@Builder
public class StockDto {
    /**
     * Уникальный идентификатор товара
     */
    private UUID uuid;
    /**
     * Наименование товара
     */
    private String name;
    /**
     * Цена товара
     */
    private BigDecimal price;
    /**
     * Количество товара
     */
    private Integer amount;
    /**
     * Категория товара
     */
    private String category;
    /**
     * Уникальный артикул товара
     */
    private String vendorCode;
    /**
     * Описание товара
     */
    private String description;
    /**
     * Время и дата обновления товара
     */
    private OffsetDateTime updateDate;
    /**
     * Время и дата создания товара
     */
    private OffsetDateTime creationDate;
}

