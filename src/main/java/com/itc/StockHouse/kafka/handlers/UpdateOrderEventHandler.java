package com.itc.StockHouse.kafka.handlers;

import com.itc.StockHouse.dto.domain.order.ProductDTO;
import com.itc.StockHouse.kafka.dto.Event;
import com.itc.StockHouse.kafka.dto.UpdateOrderEventData;
import com.itc.StockHouse.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component(Event.UPDATE_ORDER)
@RequiredArgsConstructor
public class UpdateOrderEventHandler implements KafkaEventHandler<UpdateOrderEventData> {
    private final OrderService orderService;

    @Override
    public void handle(UpdateOrderEventData event) {
        orderService.addProductsToOrder(
                event.getCustomerId(),
                event.getOrderId(),
                event.getProducts()
                        .stream()
                        .map(
                                productSchema -> ProductDTO.builder()
                                        .quantity(productSchema.getQuantity())
                                        .productId(productSchema.getProductId())
                                        .build()
                        )
                        .toList()
        );
    }
}
