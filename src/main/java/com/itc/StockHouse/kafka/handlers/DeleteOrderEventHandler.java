package com.itc.StockHouse.kafka.handlers;

import com.itc.StockHouse.kafka.dto.DeleteOrderEventData;
import com.itc.StockHouse.kafka.dto.Event;
import com.itc.StockHouse.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component(Event.DELETE_ORDER)
@RequiredArgsConstructor
public class DeleteOrderEventHandler implements KafkaEventHandler<DeleteOrderEventData> {
    private final OrderService orderService;

    @Override
    public void handle(DeleteOrderEventData event) {
        orderService.softDeleteOrder(event.getCustomerId(), event.getOrderId());
    }
}
