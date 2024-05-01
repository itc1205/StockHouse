package com.itc.StockHouse.service;

import com.itc.StockHouse.exceptions.StockVendorCodeAlreadyExistsException;
import com.itc.StockHouse.exceptions.StockNotFoundException;
import com.itc.StockHouse.model.StockEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для управления товарами на складе
 */
public interface StockService {
    /**
     * Метод для создания товара на складе
     *
     * @param stock Новый товар
     * @return Созданный товар
     * @throws StockVendorCodeAlreadyExistsException В случае наличия товара с таким же артиклем
     */
    StockEntity createStock(StockEntity stock) throws StockVendorCodeAlreadyExistsException;

    /**
     * Метод для получения товара по уникальному идентификатору (UUID)
     *
     * @param uuid Уникальный идентификатор товара
     * @return Найденный товар
     * @throws StockNotFoundException В случае отсутствия товара с таким уникальным идентификатором
     */
    StockEntity getStockByUUID(UUID uuid) throws StockNotFoundException;

    /**
     * Метод для получения всех товаров со склада
     *
     * @return Список всех товаров на складе
     */
    Page<StockEntity> getAll(Pageable pageable);

    /**
     * Метод для обновления товара на складе
     *
     * @param stock Товар, который нужно обновить
     * @return Обновленный товар
     * @throws StockNotFoundException                В случае ненахождения товара с таким же уникальным идентификатором
     * @throws StockVendorCodeAlreadyExistsException В случае конфликта артикла с уже существующим товаром
     */
    StockEntity updateStock(@NotNull StockEntity stock) throws StockNotFoundException, StockVendorCodeAlreadyExistsException;

    /**
     * Метод для обновления количества товара
     *
     * @param id        Уникальный идентификатор товара
     * @param stockAmount Новое количество товара
     * @return Обновленный товар
     * @throws StockNotFoundException В случае ненахождения товара с таким же уникальным идентификатором
     */
    StockEntity updateAmountOfStock(UUID id, Integer stockAmount) throws StockNotFoundException;

    /**
     * Метод для удаления товара на складе по уникальному идентификатору (UUID)
     *
     * @param id Уникальный идентификатор товара
     * @throws StockNotFoundException В случае ненахождения товара с таким же уникальным идентификатором
     */
    void deleteStockByUUID(UUID id) throws StockNotFoundException;
}
