package com.itc.StockHouse.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO-объект для обновления кол-ва товаров
 */
@Data
public class PatchAmountProductDTO {
    /**
     * Количество товара
     */
    @Min(0)
    @NotNull
    private Integer amount;
}
