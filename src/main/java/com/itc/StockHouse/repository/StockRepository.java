package com.itc.StockHouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.itc.StockHouse.model.StockEntity;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для товаров
 */
@Repository
public interface StockRepository extends JpaRepository<StockEntity, UUID> {
    Optional<StockEntity> findByVendorCode(String vendorCode);
}
