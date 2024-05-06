package com.itc.StockHouse.model;

import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Модель отображающая товар на складе
 */
@Getter
@Setter
@Entity
@Table(name = "STOCKS")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockEntity {
    /**
     * Уникальный идентификатор товара
     */
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    /**
     * Наименование товара
     */
    @Column(name = "name", nullable = false)
    private String name;
    /**
     * Цена товара
     */
    @Column(name = "price", nullable = false)
    private BigDecimal price;
    /**
     * Количество товара
     */
    @Column(name = "amount", nullable = false)
    private Integer amount;
    /**
     * Категория товара
     */
    @Column(name = "category", nullable = false)
    private String category;
    /**
     * Уникальный артикул товара
     */
    @Column(name = "vendor_code", unique = true)
    private String vendorCode;
    /**
     * Описание товара
     */
    @Column(name = "description", nullable = false)
    private String description;
    /**
     * Время и дата обновления товара
     */
    @Column(name = "update_date", nullable = false)
    private OffsetDateTime updateDate;
    /**
     * Время и дата создания товара
     */
    @Column(name = "creation_date", nullable = false, updatable = false)
    @CreationTimestamp
    private OffsetDateTime creationDate;
}
