package com.itc.StockHouse.service;

import com.itc.StockHouse.exceptions.ProductVendorCodeAlreadyExistsException;
import com.itc.StockHouse.exceptions.ProductNotFoundException;
import com.itc.StockHouse.model.ProductEntity;
import com.itc.StockHouse.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Реализация сервиса по управлению товарами на складе {@link ProductService}
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;

    @Override
    public ProductEntity createProduct(@NotNull ProductEntity product) throws ProductVendorCodeAlreadyExistsException {
        Optional<ProductEntity> productEntity = repository.findByVendorCode(product.getVendorCode());
        if (productEntity.isPresent()) {
            throw new ProductVendorCodeAlreadyExistsException(
                    "Товар с артикулом %s уже есть в базе данных".formatted(product.getVendorCode())
            );
        }
        return repository.save(product);
    }

    @Override
    public ProductEntity getProductByUUID(UUID id) throws ProductNotFoundException {
        return repository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Товар с UUID %s не найден!".formatted(id))
        );
    }

    @Override
    public Page<ProductEntity> getAll(Pageable pageable) {
        return repository.findAll(
                pageable
        );
    }

    @Override
    public ProductEntity updateProduct(@NotNull ProductEntity product) throws ProductNotFoundException, ProductVendorCodeAlreadyExistsException {
        ProductEntity productEntity = repository.findById(product.getId()).orElseThrow(
                () -> new ProductNotFoundException("Товар с ID %s не найден!".formatted(product.getId()))
        );

        // Если у обновляемого объекта поменялся артикул
        if (!productEntity.getVendorCode().equals(product.getVendorCode())) {
            // И если в базе данных уже существует запись с таким артикулом
            if (repository.findByVendorCode(product.getVendorCode()).isPresent()) {
                throw new ProductVendorCodeAlreadyExistsException(
                        "Товар с артикулом %s уже есть в базе данных".formatted(product.getVendorCode())
                );
            }
        }
        if (product.getAmount().equals(productEntity.getAmount())) {
            product.setUpdateDate(OffsetDateTime.now());
        }

        // BUG: По непонятной причине, поле creationDate имеет значение null
        // ДАЖЕ если мы после сохранения заново пытаемся получить значение товара из базы данных в новую переменную
        // И ДАЖЕ если мы сохраняем с помощью .saveAndFlush(); и повторяем шаг сверху
        // скорее всего причина в том, что creationDate полностью исключается из запросов UPDATE Hibernate'ом,
        // а затем еще и кешируется

        // Небольшой воркараунд - просто ставим дату создания из прошлой энтити
        product.setCreationDate(productEntity.getCreationDate());

        return repository.save(product);
    }

    @Override
    public ProductEntity updateAmountOfProduct(UUID id, Integer productAmount) throws ProductNotFoundException {
        ProductEntity productEntity = repository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Товар с ID %s не найден!".formatted(id))
        );
        productEntity.setAmount(productAmount);
        productEntity.setUpdateDate(OffsetDateTime.now());
        return repository.save(productEntity);
    }

    @Override
    public void deleteProductByUUID(UUID id) throws ProductNotFoundException {
        ProductEntity productEntity = repository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Товар с ID %s не найден!".formatted(id))
        );
        repository.delete(productEntity);
    }
}