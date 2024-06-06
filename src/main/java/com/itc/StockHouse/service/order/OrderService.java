package com.itc.StockHouse.service.order;

import com.itc.StockHouse.dto.domain.order.OrderDTO;
import com.itc.StockHouse.dto.domain.order.OrderInfoDTO;
import com.itc.StockHouse.dto.domain.order.OrderStatusDTO;
import com.itc.StockHouse.dto.domain.order.ProductDTO;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface OrderService {
    OrderDTO getOrderById(Long customerId, UUID id);

    UUID createOrder(Long customerId, OrderDTO createOrderRequest);

    void addProductsToOrder(Long customerId, UUID id, List<ProductDTO> products);

    void softDeleteOrder(Long customerId, UUID id);

    void setStatus(OrderStatusDTO orderStatusDTO, UUID orderId);

    Map<UUID, List<OrderInfoDTO>> getInfoAboutActiveOrders();
}
