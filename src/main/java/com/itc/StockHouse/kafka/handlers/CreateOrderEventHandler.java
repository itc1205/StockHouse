package com.itc.StockHouse.kafka.handlers;

import com.itc.StockHouse.dto.domain.order.OrderDTO;
import com.itc.StockHouse.dto.domain.order.ProductDTO;
import com.itc.StockHouse.kafka.dto.CreateOrderEventData;
import com.itc.StockHouse.kafka.dto.Event;
import com.itc.StockHouse.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component(Event.CREATE_ORDER)
@RequiredArgsConstructor
public class CreateOrderEventHandler implements KafkaEventHandler<CreateOrderEventData> {

    private final OrderService orderService;

    @Override
    public void handle(CreateOrderEventData event) {
        orderService.createOrder(event.getCustomerId(),
                OrderDTO.builder()
                        .deliveryAddress(event.getDeliveryAddress())
                        .products(event.getProducts().stream()
                                .map(productSchema ->
                                        ProductDTO.builder()
                                            .productId(productSchema.getProductId())
                                            .quantity(productSchema.getQuantity())
                                            .build()
                                ).toList()
                        )
                        .build()
        );
    }
}
