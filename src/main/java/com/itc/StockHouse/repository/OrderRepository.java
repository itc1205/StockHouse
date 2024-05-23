package com.itc.StockHouse.repository;

import com.itc.StockHouse.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    @Query("SELECT oe FROM OrderEntity oe WHERE oe.status=OrderStatus.CREATED OR oe.status=OrderStatus.CONFIRMED")
    List<OrderEntity> findAllActiveOrders();
}
