package com.itc.StockHouse.dto.schema.order;

import com.itc.StockHouse.dto.domain.order.OrderStatusDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SetOrderStatusSchema {
    private OrderStatusDTO status;
}
