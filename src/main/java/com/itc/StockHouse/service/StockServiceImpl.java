package com.itc.StockHouse.service;

import com.itc.StockHouse.exceptions.StockVendorCodeAlreadyExistsException;
import com.itc.StockHouse.exceptions.StockNotFoundException;
import com.itc.StockHouse.model.StockEntity;
import com.itc.StockHouse.repository.StockRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Реализация сервиса по управлению товарами на складе {@link com.itc.StockHouse.service.StockService}
 */
@Service
public class StockServiceImpl implements StockService {
    @Autowired
    private StockRepository repository;

    @Override
    public StockEntity createStock(@NotNull StockEntity stock) throws StockVendorCodeAlreadyExistsException {
        Optional<StockEntity> stockEntity = repository.findByVendorCode(stock.getVendorCode());
        if (stockEntity.isPresent()) {
            throw new StockVendorCodeAlreadyExistsException(
                    "Товар с артикулом %s уже есть в базе данных".formatted(stock.getVendorCode())
            );
        }
        return repository.save(stock);
    }

    @Override
    public StockEntity getStockByUUID(UUID uuid) throws StockNotFoundException {
        return repository.findById(uuid).orElseThrow(
                () -> new StockNotFoundException("Товар с UUID %s не найден!".formatted(uuid))
        );
    }

    @Override
    public Page<StockEntity> getAll(Pageable pageable) {
        return repository.findAll(
                pageable
        );
    }

    @Override
    public StockEntity updateStock(@NotNull StockEntity stock) throws StockNotFoundException, StockVendorCodeAlreadyExistsException {
        StockEntity stockEntity = repository.findById(stock.getId()).orElseThrow(
                () -> new StockNotFoundException("Товар с ID %s не найден!".formatted(stock.getId()))
        );

        // Если у обновляемого объекта поменялся артикул
        if (!stockEntity.getVendorCode().equals(stock.getVendorCode())) {
            // И если в базе данных уже существует запись с таким артикулом
            if (repository.findByVendorCode(stock.getVendorCode()).isPresent()) {
                throw new StockVendorCodeAlreadyExistsException(
                        "Товар с артикулом %s уже есть в базе данных".formatted(stock.getVendorCode())
                );
            }
        }

        // BUG: По непонятной причине, поле creationDate имеет значение null
        // ДАЖЕ если мы после сохранения заново пытаемся получить значение товара из базы данных в новую переменную
        // И ДАЖЕ если мы сохраняем с помощью .saveAndFlush(); и повторяем шаг сверху
        // скорее всего причина в том, что creationDate полностью исключается из запросов UPDATE Hibernate'ом,
        // а затем еще и кешируется

        // Небольшой воркараунд - просто ставим дату создания из прошлой энтити
        stock.setCreationDate(stockEntity.getCreationDate());

        return repository.save(stock);
    }

    @Override
    public StockEntity updateAmountOfStock(UUID id, Integer stockAmount) throws StockNotFoundException {
        StockEntity stockEntity = repository.findById(id).orElseThrow(
                () -> new StockNotFoundException("Товар с ID %s не найден!".formatted(id))
        );
        stockEntity.setAmount(stockAmount);
        return repository.save(stockEntity);
    }

    @Override
    public void deleteStockByUUID(UUID id) throws StockNotFoundException {
        StockEntity stockEntity = repository.findById(id).orElseThrow(
                () -> new StockNotFoundException("Товар с ID %s не найден!".formatted(id))
        );
        repository.delete(stockEntity);
    }
}