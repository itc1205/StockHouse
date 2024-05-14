package com.itc.StockHouse.repository;

import com.itc.StockHouse.dto.domain.order.ProductDTO;
import com.itc.StockHouse.model.OrderedProductEntity;
import com.itc.StockHouse.model.OrderedProductKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OrderedProductRepository extends JpaRepository<OrderedProductEntity, OrderedProductKey> {
    @Query("SELECT new com.itc.StockHouse.dto.domain.order.ProductDTO(op.id.productId, p.name, op.quantity, op.price) FROM OrderedProductEntity op INNER JOIN ProductEntity p ON op.id.productId=p.id WHERE op.id.orderId=?1")
    List<ProductDTO> findByOrderId(UUID orderId);
}
