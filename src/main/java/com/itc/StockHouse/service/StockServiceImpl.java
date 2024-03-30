package com.itc.StockHouse.service;

import com.itc.StockHouse.exceptions.StockVendorCodeAlreadyExistsException;
import com.itc.StockHouse.exceptions.StockNotFoundException;
import com.itc.StockHouse.model.StockEntity;
import com.itc.StockHouse.repository.StockRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
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
            throw new StockVendorCodeAlreadyExistsException();
        }
        return repository.save(stock);
    }

    @Override
    public StockEntity getStockByUUID(UUID uuid) throws StockNotFoundException {
        return repository.findById(uuid).orElseThrow(StockNotFoundException::new);
    }

    @Override
    public List<StockEntity> getAll() {
        return repository.findAll();
    }

    @Override
    public StockEntity updateStock(@NotNull StockEntity stock) throws StockNotFoundException, StockVendorCodeAlreadyExistsException {
        StockEntity stockEntity = repository.findById(stock.getUuid()).orElseThrow(StockNotFoundException::new);

        // Если у обновляемого объекта внезапно поменялся артикул
        if (!stockEntity.getVendorCode().equals(stock.getVendorCode())) {
            // И если в базе данных уже существует запись с таким артикулом
            if (repository.findByVendorCode(stock.getVendorCode()).isPresent()) {
                throw new StockVendorCodeAlreadyExistsException();
            }
        }

        return repository.save(stock);
    }

    @Override
    public StockEntity updateAmountOfStock(UUID uuid, Integer stockAmount) throws StockNotFoundException {
        StockEntity stockEntity = repository.findById(uuid).orElseThrow(StockNotFoundException::new);
        stockEntity.setAmount(stockAmount);
        stockEntity.setUpdateDate(OffsetDateTime.now());
        return repository.save(stockEntity);
    }

    @Override
    public void deleteStockByUUID(UUID uuid) throws StockNotFoundException {
        Optional<StockEntity> stockEntity = repository.findById(uuid);
        if (stockEntity.isEmpty()) {
            throw new StockNotFoundException();
        }
        repository.delete(stockEntity.get());
    }
}

