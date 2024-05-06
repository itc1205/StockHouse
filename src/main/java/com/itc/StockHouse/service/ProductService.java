package com.itc.StockHouse.service;

import com.itc.StockHouse.exceptions.ProductVendorCodeAlreadyExistsException;
import com.itc.StockHouse.exceptions.ProductNotFoundException;
import com.itc.StockHouse.model.ProductEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Сервис для управления товарами на складе
 */
public interface ProductService {
    /**
     * Метод для создания товара на складе
     *
     * @param product Новый товар
     * @return Созданный товар
     * @throws ProductVendorCodeAlreadyExistsException В случае наличия товара с таким же артиклем
     */
    ProductEntity createProduct(ProductEntity product) throws ProductVendorCodeAlreadyExistsException;

    /**
     * Метод для получения товара по уникальному идентификатору (UUID)
     *
     * @param id Уникальный идентификатор товара
     * @return Найденный товар
     * @throws ProductNotFoundException В случае отсутствия товара с таким уникальным идентификатором
     */
    ProductEntity getProductByUUID(UUID id) throws ProductNotFoundException;

    /**
     * Метод для получения всех товаров со склада
     *
     * @return Список всех товаров на складе
     */
    Page<ProductEntity> getAll(Pageable pageable);

    /**
     * Метод для обновления товара на складе
     *
     * @param product Товар, который нужно обновить
     * @return Обновленный товар
     * @throws ProductNotFoundException                В случае ненахождения товара с таким же уникальным идентификатором
     * @throws ProductVendorCodeAlreadyExistsException В случае конфликта артикла с уже существующим товаром
     */
    ProductEntity updateProduct(@NotNull ProductEntity product) throws ProductNotFoundException, ProductVendorCodeAlreadyExistsException;

    /**
     * Метод для обновления количества товара
     *
     * @param id        Уникальный идентификатор товара
     * @param productAmount Новое количество товара
     * @return Обновленный товар
     * @throws ProductNotFoundException В случае ненахождения товара с таким же уникальным идентификатором
     */
    ProductEntity updateAmountOfProduct(UUID id, Integer productAmount) throws ProductNotFoundException;

    /**
     * Метод для удаления товара на складе по уникальному идентификатору (UUID)
     *
     * @param id Уникальный идентификатор товара
     * @throws ProductNotFoundException В случае ненахождения товара с таким же уникальным идентификатором
     */
    void deleteProductByUUID(UUID id) throws ProductNotFoundException;
}
