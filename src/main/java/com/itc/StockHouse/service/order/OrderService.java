package com.itc.StockHouse.service.order;

import com.itc.StockHouse.dto.domain.customer.CustomerDTO;
import com.itc.StockHouse.dto.domain.order.OrderDTO;
import com.itc.StockHouse.dto.domain.order.OrderStatusDTO;
import com.itc.StockHouse.dto.domain.order.ProductDTO;
import com.itc.StockHouse.exceptions.InsufficientRightsException;
import com.itc.StockHouse.exceptions.ProductNotFoundException;
import com.itc.StockHouse.exceptions.customer.CustomerNotFoundException;
import com.itc.StockHouse.exceptions.order.InsufficientProductsException;
import com.itc.StockHouse.exceptions.order.OrderCantBeChangedException;
import com.itc.StockHouse.exceptions.order.OrderCantBeDeletedException;
import com.itc.StockHouse.exceptions.order.OrderNotFoundException;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderDTO getOrderById(CustomerDTO customer, UUID id) throws OrderNotFoundException, InsufficientRightsException;
    UUID createOrder(CustomerDTO customer, OrderDTO order) throws InsufficientProductsException, ProductNotFoundException, CustomerNotFoundException;
    void addProductsToOrder(CustomerDTO customer, UUID id, List<ProductDTO> products) throws InsufficientProductsException, OrderNotFoundException, ProductNotFoundException, InsufficientRightsException, OrderCantBeChangedException;
    void softDeleteOrder(CustomerDTO customer, UUID id) throws OrderCantBeDeletedException, OrderNotFoundException, InsufficientRightsException;
    void setStatus(CustomerDTO customerDTO, OrderStatusDTO orderStatusDTO, UUID id) throws OrderNotFoundException, InsufficientRightsException;
}
