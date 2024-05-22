package com.itc.StockHouse.controller;

import com.itc.StockHouse.dto.domain.order.OrderDTO;
import com.itc.StockHouse.dto.domain.order.ProductDTO;
import com.itc.StockHouse.dto.schema.order.CreateOrderSchema;
import com.itc.StockHouse.dto.schema.order.ProductSchema;
import com.itc.StockHouse.dto.schema.order.SetOrderStatusSchema;
import com.itc.StockHouse.service.order.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;


    @GetMapping("/{id}")
    OrderDTO getOrderById(@RequestHeader Long customerId, UUID orderId) {
        return orderService.getOrderById(customerId, orderId);
    }

    @PostMapping("/")
    UUID createOrder(@RequestHeader Long customerId, @RequestBody CreateOrderSchema order) {
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
        return orderService.createOrder(customerId, orderDTO);
    }

    @PatchMapping("/{orderId}")
    void updateOrder(@RequestHeader Long customerId, @RequestBody List<ProductSchema> products, @PathVariable UUID orderId) {
        List<ProductDTO> productDTOList = products.stream()
                .map(productSchema -> ProductDTO.builder()
                        .productId(productSchema.getProductId())
                        .quantity(productSchema.getQuantity())
                        .build())
                .toList();
        orderService.addProductsToOrder(customerId, orderId, productDTOList);
    }

    @DeleteMapping("/{orderId}")
    void softDeleteOrder(@RequestHeader Long customerId, @PathVariable UUID orderId) {
        orderService.softDeleteOrder(customerId, orderId);
    }

    @PatchMapping("/{orderId}/status")
    void updateOrderStatus(@RequestHeader Long customerId, @RequestBody  @Valid SetOrderStatusSchema orderStatusSchema, @PathVariable UUID orderId) {
        orderService.setStatus(customerId, orderStatusSchema.getStatus(), orderId);
    }
}
