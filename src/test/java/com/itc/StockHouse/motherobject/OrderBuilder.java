package com.itc.StockHouse.motherobject;


import com.itc.StockHouse.model.CustomerEntity;
import com.itc.StockHouse.model.OrderEntity;
import com.itc.StockHouse.model.OrderStatus;
import com.itc.StockHouse.model.OrderedProductEntity;

import java.util.ArrayList;
import java.util.List;

public class OrderBuilder {
    private static final OrderStatus DEFAULT_STATUS = OrderStatus.CREATED;
    private static final String DEFAULT_DELIVERY_ADDRESS = "DEFAULT_DELIVERY_ADDRESS";
    private static final List<OrderedProductEntity> DEFAULT_ORDERED_PRODUCTS = new ArrayList<>();


    private OrderStatus status = DEFAULT_STATUS;
    private String deliveryAddress = DEFAULT_DELIVERY_ADDRESS;
    private List<OrderedProductEntity> orderedProducts = DEFAULT_ORDERED_PRODUCTS;
    private CustomerEntity customer;

    public OrderBuilder withStatus(OrderStatus status) {
        this.status = status;
        return this;
    }

    public OrderBuilder withDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
        return this;
    }

    public OrderBuilder withOrderedProducts(List<OrderedProductEntity> orderedProducts) {
        this.orderedProducts = orderedProducts;
        return this;
    }

    public OrderBuilder withCustomer(CustomerEntity customer) {
        this.customer = customer;
        return this;
    }

    public OrderEntity build() {
        return OrderEntity.builder()
                .deliveryAddress(deliveryAddress)
                .status(status)
                .products(orderedProducts)
                .customer(customer)
                .build();
    }
}
