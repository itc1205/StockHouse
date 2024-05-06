package com.itc.StockHouse.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO-объект содержащий необходимую информацию для создания товара на складе
 */
@Data
public class CreateProductDto {
    /**
     * Наименование товара
     */
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    /**
     * Цена товара
     */
    @NotNull(message = "Цена товаров не может быть пустой")
    @Digits(integer = 20, fraction = 2, message = "Неправильный формат цены")
    @DecimalMin(value = "0", message = "Цена не может быть ниже нуля")
    private BigDecimal price;
    /**
     * Количество товара
     */
    @NotNull(message = "Количество товаров не может быть пустым")
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
