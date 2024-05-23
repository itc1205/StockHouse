package com.itc.StockHouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.itc.StockHouse.model.ProductEntity;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Репозиторий для товаров
 */
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID>, PagingAndSortingRepository<ProductEntity, UUID>, JpaSpecificationExecutor<ProductEntity> {
    Optional<ProductEntity> findByVendorCode(String vendorCode);

    @Query("SELECT p FROM ProductEntity p WHERE p.id IN ?1")
    Stream<ProductEntity> streamAllByIds(Set<UUID>ids);
}
