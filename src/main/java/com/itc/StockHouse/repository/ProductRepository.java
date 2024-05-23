package com.itc.StockHouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.itc.StockHouse.model.ProductEntity;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для товаров
 */
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID>, PagingAndSortingRepository<ProductEntity, UUID> {
    Optional<ProductEntity> findByVendorCode(String vendorCode);
}
