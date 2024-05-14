package com.itc.StockHouse.service.order;

import com.itc.StockHouse.dto.domain.customer.CustomerDTO;
import com.itc.StockHouse.dto.domain.order.OrderDTO;
import com.itc.StockHouse.dto.domain.order.OrderStatusDTO;
import com.itc.StockHouse.dto.domain.order.ProductDTO;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderDTO getOrderById(CustomerDTO customer, UUID id);

    UUID createOrder(CustomerDTO customer, OrderDTO order);

    void addProductsToOrder(CustomerDTO customer, UUID id, List<ProductDTO> products);

    void softDeleteOrder(CustomerDTO customer, UUID id);

    void setStatus(CustomerDTO customerDTO, OrderStatusDTO orderStatusDTO, UUID id);
}
