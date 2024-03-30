package com.itc.StockHouse.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;


/**
 * DTO-объект содержащий необходимую информацию для обновления товара на складе
 */
@Data
public class UpdateStockDto {
    /**
     * Уникальный идентификатор товара
     */
    @org.hibernate.validator.constraints.UUID(message = "Некорректный UUID")
    @NotBlank(message = "UUID не может быть пустым")
    private UUID uuid;
    /**
     * Наименование товара
     */
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    /**
     * Цена товара
     */
    @NotBlank(message = "Цена не может быть пустой")
    @Min(value = 1, message = "Цена может быть выше нуля")
    private BigDecimal price;
    /**
     * Количество товара
     */
    @NotBlank(message = "Количество товаров не может быть пустым")
    @Min(value = 0, message = "Количество товаров не может быть ниже нуля")
    private Integer amount;
    /**
     * Категория товара
     */
    @NotBlank(message = "Категория не может быть пустой")
    private String category;
    /**
     * Уникальный артикул товара
     */
    @NotBlank(message = "Артикул не может быть пустым")
    private String vendorCode;
    /**
     * Описание товара
     */
    @NotBlank(message = "Описание не может быть пустым")
    private String description;
}
