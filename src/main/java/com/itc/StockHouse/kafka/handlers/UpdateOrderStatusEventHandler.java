package com.itc.StockHouse.kafka.handlers;

import com.itc.StockHouse.dto.domain.order.OrderStatusDTO;
import com.itc.StockHouse.kafka.dto.Event;
import com.itc.StockHouse.kafka.dto.UpdateOrderStatusEventData;
import com.itc.StockHouse.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component(Event.UPDATE_ORDER_STATUS)
@RequiredArgsConstructor
public class UpdateOrderStatusEventHandler implements KafkaEventHandler<UpdateOrderStatusEventData> {
    private final OrderService orderService;


    @Override
    public void handle(UpdateOrderStatusEventData event) {
        orderService.setStatus(OrderStatusDTO.valueOf(event.getEvent()), event.getOrderId());
    }
}
