package com.itc.StockHouse.repository;

import com.itc.StockHouse.dto.domain.order.ProductDTO;
import com.itc.StockHouse.model.OrderedProductEntity;
import com.itc.StockHouse.model.OrderedProductKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderedProductRepository extends JpaRepository<OrderedProductEntity, OrderedProductKey> {
    @Query("SELECT new com.itc.StockHouse.dto.domain.order.ProductDTO(op.product.id, p.name, op.quantity, op.price) FROM OrderedProductEntity op INNER JOIN ProductEntity p ON op.product.id=p.id WHERE op.order.id=?1")
    List<ProductDTO> findProjectionByOrderId(UUID orderId);

    Optional<OrderedProductEntity> findByOrder_Id(UUID orderId);

    Optional<OrderedProductEntity> findByProduct_Id(UUID productId);
}
