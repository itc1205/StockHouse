package com.itc.StockHouse.controller;

import com.itc.StockHouse.dto.domain.customer.CustomerDTO;
import com.itc.StockHouse.dto.domain.order.OrderDTO;
import com.itc.StockHouse.dto.domain.order.ProductDTO;
import com.itc.StockHouse.dto.schema.order.CreateOrderSchema;
import com.itc.StockHouse.dto.schema.order.ProductSchema;
import com.itc.StockHouse.dto.schema.order.SetOrderStatusSchema;
import com.itc.StockHouse.exceptions.InsufficientRightsException;
import com.itc.StockHouse.exceptions.customer.CustomerNotFoundException;
import com.itc.StockHouse.exceptions.order.InsufficientProductsException;
import com.itc.StockHouse.exceptions.order.OrderCantBeChangedException;
import com.itc.StockHouse.exceptions.order.OrderCantBeDeletedException;
import com.itc.StockHouse.exceptions.order.OrderNotFoundException;
import com.itc.StockHouse.service.customer.CustomerService;
import com.itc.StockHouse.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final CustomerService customerService;


    @GetMapping("/{id}")
    OrderDTO getOrderById(@RequestHeader Long customerId, UUID id) throws OrderNotFoundException, CustomerNotFoundException, InsufficientRightsException {
        CustomerDTO customerDTO = customerService.getCustomer(customerId);
        return orderService.getOrderById(customerDTO, id);
    }

    @PostMapping("/")
    UUID createOrder(@RequestHeader Long customerId, @RequestBody CreateOrderSchema order) throws CustomerNotFoundException, InsufficientProductsException {
        CustomerDTO customerDTO = customerService.getCustomer(customerId);
        OrderDTO orderDTO = OrderDTO.builder()
                .deliveryAddress(order.getDeliveryAddress())
                .products(order.getProducts()
                        .stream()
                        .map(product -> ProductDTO.builder()
                                .productId(product.getProductId())
                                .quantity(product.getQuantity())
                                .build())
                        .toList())
                        .build();
        return orderService.createOrder(customerDTO, orderDTO);
    }

    @PatchMapping("/{orderId}")
    void updateOrder(@RequestHeader Long customerId, @RequestBody List<ProductSchema> products, @PathVariable UUID orderId) throws CustomerNotFoundException, OrderNotFoundException, InsufficientProductsException, InsufficientRightsException, OrderCantBeChangedException {
        CustomerDTO customerDTO = customerService.getCustomer(customerId);
        List<ProductDTO> productDTOList = products.stream()
                .map(productSchema -> ProductDTO.builder()
                        .productId(productSchema.getProductId())
                        .quantity(productSchema.getQuantity())
                        .build())
                .toList();
        orderService.addProductsToOrder(customerDTO, orderId, productDTOList);
    }

    @DeleteMapping("/{orderId}")
    void softDeleteOrder(@RequestHeader Long customerId, @PathVariable UUID orderId) throws OrderNotFoundException, OrderCantBeDeletedException, CustomerNotFoundException, InsufficientRightsException {
        CustomerDTO customerDTO = customerService.getCustomer(customerId);
        orderService.softDeleteOrder(customerDTO, orderId);
    }

    @PatchMapping("/{orderId}/status")
    void updateOrderStatus(@RequestHeader Long customerId, @RequestBody SetOrderStatusSchema orderStatusSchema, @PathVariable UUID orderId) throws CustomerNotFoundException, OrderNotFoundException, InsufficientRightsException {
        CustomerDTO customerDTO = customerService.getCustomer(customerId);
        orderService.setStatus(customerDTO, orderStatusSchema.getStatus(), orderId);
    }
}
